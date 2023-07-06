package com.lintang.netflik.paymentservice.command.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.protobuf.Any;
import com.lintang.netflik.paymentservice.broker.message.CompensatingOrderSubscriptionMessage;
import com.lintang.netflik.paymentservice.broker.message.PaymentCanceledMessage;
import com.lintang.netflik.paymentservice.broker.message.PaymentTransactionErrorMessage;
import com.lintang.netflik.paymentservice.broker.message.PaymentValidatedMessage;
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

   /*
    insert outbox && insert payment to db
    if transactionstatus rejected -> refund / cancel payment
    if transactionstatus is settlement -> insert PaymentValidatedMessage to outbox table
    */
    @Transactional
    public void validatePayment(Map<String, Any> mapNotificationMidtrans) throws JsonProcessingException {

        String orderId = (String) SerializationUtils.deserialize(mapNotificationMidtrans.get("order_id")
                .getValue().toByteArray());
        String transactionStatus = (String) SerializationUtils.deserialize(mapNotificationMidtrans.get("transaction_status")
                .getValue().toByteArray());
        String fraudStatus = (String) SerializationUtils.deserialize(mapNotificationMidtrans.get("fraud_status")
                .getValue().toByteArray());
        String transactionId = ((String)  SerializationUtils.deserialize(mapNotificationMidtrans.get("transaction_id").getValue().toByteArray()));

        if (transactionStatus.equals("capture")) {
            if (fraudStatus.equals("challenge")) {
                // TODO set transaction status on your database to 'challenge' e.g: 'Payment status challenged. Please take action on your Merchant Administration Portal
                    // save outbox message and send to t.saga.order.outbox.payment-validate.response
                    // order service update database status to cancelled

                    paymentSagaAction.savePayment(mapNotificationMidtrans);
                    PaymentCanceledMessage paymentCanceledMessage = PaymentCanceledMessage.builder()
                            .orderStatus(OrderStatus.CANCELLED)
                            .paymentId((String) SerializationUtils.deserialize(mapNotificationMidtrans.get("transaction_id").getValue().toByteArray()))
                            .failureMessages("")
                            .grossAmunt((String) SerializationUtils.deserialize(mapNotificationMidtrans.get("gross_amount").getValue().toByteArray()))
                            .orderId(orderId)
                            .build();
                    var paymentOutbox = outboxAction.insertOutbox(
                            "payment-validate.response",
                            orderId,
                            OutboxEventType.CANCELLED_PAYMENT, paymentCanceledMessage, SagaStatus.PROCESSING
                    );
                    outboxAction.deleteOutbox(paymentOutbox);
                    // cancel payment
                    cancelPayment(mapNotificationMidtrans);
            } else if (fraudStatus.equals("accept")) {
                // TODO set transaction status on your database to 'success'
                paymentSagaAction.savePayment(mapNotificationMidtrans);
                PaymentValidatedMessage paymentValidatedMessage = PaymentValidatedMessage.builder()
                        .orderStatus(OrderStatus.PAID)
                        .paymentId((String) SerializationUtils.deserialize(mapNotificationMidtrans.get("transaction_id").getValue().toByteArray()))
                        .failureMessages("")
                        .grossAmunt((String) SerializationUtils.deserialize(mapNotificationMidtrans.get("gross_amount").getValue().toByteArray()))
                        .orderId(orderId)
                        .build();
                var paymentOutbox = outboxAction.insertOutbox(
                        "payment-validate.response",
                        orderId,
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
                    .paymentId((String) SerializationUtils.deserialize(mapNotificationMidtrans.get("transaction_id").getValue().toByteArray()))
                    .failureMessages("")
                    .grossAmunt((String) SerializationUtils.deserialize(mapNotificationMidtrans.get("gross_amount").getValue().toByteArray()))
                    .orderId(orderId)
                    .build();
            var paymentOutbox = outboxAction.insertOutbox(
                    "payment-validate.response",
                    orderId,
                    OutboxEventType.CANCELLED_PAYMENT, paymentCanceledMessage, SagaStatus.PROCESSING
            );
            outboxAction.deleteOutbox(paymentOutbox);

        } else if (transactionStatus.equals("pending")) {
            // TODO set transaction status on your database to 'pending' / waiting payment
            paymentSagaAction.savePayment(mapNotificationMidtrans);
        }
    }

    @Transactional
    public void compensatingPaymentTransaction(CompensatingOrderSubscriptionMessage compensatingOrderSubscriptionMessage) {
        String paymentId = compensatingOrderSubscriptionMessage.getOrder().getPaymentId();
        String orderId=  compensatingOrderSubscriptionMessage.getOrder().getId().toString();
        String grossAmount = compensatingOrderSubscriptionMessage.getOrder().getPrice().toString();
        // refund
        refundPayment(orderId, grossAmount);

        // delete payment data in db
        paymentSagaAction.deletePaymentById(paymentId);
    }


//    cancel payment , if transactionStatus pending/capture
    public void  cancelPayment (Map<String, Any> mapNotificationMidtrans) {
        OkHttpClient client = new OkHttpClient();
        String orderId = (String) SerializationUtils.deserialize(mapNotificationMidtrans.get("order_id")
                .getValue().toByteArray());

        Request request = new Request.Builder()
                .url("https://api.sandbox.midtrans.com/v2/" + orderId + "/cancel")
                .post(null)
                .addHeader("accept", "application/json")
                .build();

        try {
            Response response = client.newCall(request).execute();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void refundPayment(Map<String, Any> mapNotificationMidtrans) {
        OkHttpClient client = new OkHttpClient();
        String orderId = (String) SerializationUtils.deserialize(mapNotificationMidtrans.get("order_id")
                .getValue().toByteArray());

        MediaType mediaType = MediaType.parse("application/json");
        String grossAmount = (String) SerializationUtils.deserialize(mapNotificationMidtrans.get("gross_amount")
                .getValue().toByteArray());
        RequestBody body =
                RequestBody.create(mediaType, "{\"refund_key\":\"reference1\",\"amount\":" + grossAmount + ",\"reason\":\"for some reason\"}");
        Request request = new Request.Builder()
                .url("https://api.sandbox.midtrans.com/v2/" + orderId + "/refund")
                .post(body)
                .addHeader("accept", "application/json")
                .addHeader("content-type", "application/json")
                .build();

        try {
            Response response = client.newCall(request).execute();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void refundPayment(String orderId, String grossAmount ) {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body =
                RequestBody.create(mediaType, "{\"refund_key\":\"reference1\",\"amount\":" + grossAmount + ",\"reason\":\"for some reason\"}");
        Request request = new Request.Builder()
                .url("https://api.sandbox.midtrans.com/v2/" + orderId + "/refund")
                .post(body)
                .addHeader("accept", "application/json")
                .addHeader("content-type", "application/json")
                .build();

        try {
            Response response = client.newCall(request).execute();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }


}
