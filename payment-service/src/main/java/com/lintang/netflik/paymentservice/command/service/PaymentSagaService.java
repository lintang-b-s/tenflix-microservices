package com.lintang.netflik.paymentservice.command.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.protobuf.Any;
import com.lintang.netflik.paymentservice.broker.message.CompensatingOrderSubscriptionMessage;
import com.lintang.netflik.paymentservice.broker.message.PaymentCanceledMessage;
import com.lintang.netflik.paymentservice.broker.message.PaymentTransactionErrorMessage;
import com.lintang.netflik.paymentservice.broker.message.PaymentValidatedMessage;
import com.lintang.netflik.paymentservice.command.action.MidtransAction;
import com.lintang.netflik.paymentservice.command.action.PaymentOutboxAction;
import com.lintang.netflik.paymentservice.command.action.PaymentSagaAction;
import com.lintang.netflik.paymentservice.entity.OrderStatus;
import com.lintang.netflik.paymentservice.entity.OutboxEventType;
import com.lintang.netflik.paymentservice.entity.PaymentEntity;
import com.lintang.netflik.paymentservice.entity.SagaStatus;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.SerializationUtils;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;


@Service
public class PaymentSagaService {

    @Autowired
    private PaymentSagaAction paymentSagaAction;

    @Autowired
    private PaymentOutboxAction outboxAction;

    @Autowired
    private MidtransAction midtransAction;

   /*
    insert outbox && insert payment to db
    if transactionstatus rejected -> refund / cancel payment
    if transactionstatus is settlement/capture -> insert PaymentValidatedMessage to outbox table
    */
    @Transactional
    public void validatePayment(PaymentEntity mapNotificationMidtrans) throws JsonProcessingException {

        String transactionStatus = mapNotificationMidtrans.getTransactionStatus();
        String fraudStatus = mapNotificationMidtrans.getFraudStatus();
        if (transactionStatus.equals("capture") || transactionStatus.equals("settlement")) { // settlemen kalo bank transfer, capture kalo credit card
            if (fraudStatus.equals("challenge")) {
                // TODO set transaction status on your database to 'challenge' e.g: 'Payment status challenged. Please take action on your Merchant Administration Portal
                    // save outbox message and send to t.saga.order.outbox.payment-validate.response
                    // order service update database status to cancelled

                    paymentSagaAction.savePayment(mapNotificationMidtrans);
                    PaymentCanceledMessage paymentCanceledMessage = PaymentCanceledMessage.builder()
                            .orderStatus(OrderStatus.CANCELLED)
                            .paymentId(mapNotificationMidtrans.getTransactionId())
                            .failureMessages("")
                            .grossAmunt(mapNotificationMidtrans.getGrossAmount())
                            .orderId(mapNotificationMidtrans.getOrderId())
                            .build();
                    var paymentOutbox = outboxAction.insertOutbox(
                            "payment-validate.response",
                            mapNotificationMidtrans.getOrderId(),
                            OutboxEventType.CANCELLED_PAYMENT, paymentCanceledMessage, SagaStatus.PROCESSING
                    );
                    outboxAction.deleteOutbox(paymentOutbox);
                    // cancel payment
                    midtransAction.cancelPayment(mapNotificationMidtrans);
            } else if (fraudStatus.equals("accept")) {
                // TODO set transaction status on your database to 'success'
                paymentSagaAction.savePayment(mapNotificationMidtrans);
                PaymentValidatedMessage paymentValidatedMessage = PaymentValidatedMessage.builder()
                        .orderStatus(OrderStatus.PAID)
                        .paymentId(mapNotificationMidtrans.getTransactionId())
                        .failureMessages("")
                        .grossAmunt(mapNotificationMidtrans.getGrossAmount())
                        .orderId(mapNotificationMidtrans.getOrderId())
                        .build();
                var paymentOutbox = outboxAction.insertOutbox(
                        "payment-validate.response",
                        mapNotificationMidtrans.getOrderId(),
                        OutboxEventType.VALIDATED_PAYMENT, paymentValidatedMessage, SagaStatus.PROCESSING
                );
                outboxAction.deleteOutbox(paymentOutbox);
            }
        } else if (transactionStatus.equals("cancel") || transactionStatus.equals("deny") || transactionStatus.equals("expire")) {
            // TODO set transaction status on your database to 'failure'
            // change order status to cancelled and dont continue saga
            paymentSagaAction.savePayment(mapNotificationMidtrans);
            PaymentCanceledMessage paymentCanceledMessage = PaymentCanceledMessage.builder()
                    .orderStatus(OrderStatus.CANCELLED)
                    .paymentId(mapNotificationMidtrans.getTransactionId())
                    .failureMessages("")
                    .grossAmunt(mapNotificationMidtrans.getGrossAmount())
                    .orderId( mapNotificationMidtrans.getOrderId())
                    .build();
            var paymentOutbox = outboxAction.insertOutbox(
                    "payment-validate.response",
                    mapNotificationMidtrans.getOrderId(),
                    OutboxEventType.CANCELLED_PAYMENT, paymentCanceledMessage, SagaStatus.PROCESSING
            );
            outboxAction.deleteOutbox(paymentOutbox);

        } else if (transactionStatus.equals("pending")) { // if payment not already exist save payment in database
            // TODO set transaction status on your database to 'pending' / waiting payment
            Optional<PaymentEntity> paymentAlreadyExists = this.paymentSagaAction.getPaymentById(mapNotificationMidtrans.getId());
            if (!paymentAlreadyExists.isPresent()) {
                paymentSagaAction.savePayment(mapNotificationMidtrans);
            }
        }
    }

    @Transactional
    public void compensatingPaymentTransaction(CompensatingOrderSubscriptionMessage compensatingOrderSubscriptionMessage) {
        String paymentId = compensatingOrderSubscriptionMessage.getOrder().getPaymentId();
        String orderId=  compensatingOrderSubscriptionMessage.getOrder().getId().toString();
        String grossAmount = compensatingOrderSubscriptionMessage.getOrder().getPrice().toString();
        // refund
        midtransAction.refundPayment(orderId, grossAmount);

        // delete payment data in db
        paymentSagaAction.deletePaymentById(paymentId);
    }





}
