package com.lintang.netflik.paymentservice.command.action;

import com.google.protobuf.Any;
import com.lintang.netflik.paymentservice.broker.message.PaymentTransactionErrorMessage;
import com.lintang.netflik.paymentservice.broker.message.PaymentValidatedMessage;
import com.lintang.netflik.paymentservice.entity.PaymentEntity;
import com.lintang.netflik.paymentservice.entity.VirtualAccount;
import com.lintang.netflik.paymentservice.repository.PaymentJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.SerializationUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Component
public class PaymentSagaAction {
    @Autowired
    private PaymentJpaRepository paymentJpaRepository;



    public void savePayment(Map<String, Any> mapNotificationMidtrans) {
        List<VirtualAccount> vaMid =(List<VirtualAccount>) SerializationUtils.deserialize(mapNotificationMidtrans.get("va_numbers").getValue().toByteArray());

        PaymentEntity payment = PaymentEntity.builder()
                .id((String)  SerializationUtils.deserialize(mapNotificationMidtrans.get("transaction_id").getValue().toByteArray()))
                .status((String)  SerializationUtils.deserialize(mapNotificationMidtrans.get("transaction_status").getValue().toByteArray()))
                .grossAmount((String) SerializationUtils.deserialize(mapNotificationMidtrans.get("gross_amount").getValue().toByteArray()))
                .transactionTime((String)  SerializationUtils.deserialize(mapNotificationMidtrans.get("transaction_time").getValue().toByteArray()))
                .paymentType((String)  SerializationUtils.deserialize(mapNotificationMidtrans.get("payment_type").getValue().toByteArray()))
                .settlementType((String)  SerializationUtils.deserialize(mapNotificationMidtrans.get("settlement_time").getValue().toByteArray()))
                .fraudStatus((String)  SerializationUtils.deserialize(mapNotificationMidtrans.get("fraud_status").getValue().toByteArray()))
                .orderId((String)  SerializationUtils.deserialize(mapNotificationMidtrans.get("order_id").getValue().toByteArray()))
                .bank(vaMid.get(0).getBank())
                .vaNumber(vaMid.get(0).getVaNumber()).currency((String)  SerializationUtils.deserialize(mapNotificationMidtrans.get("currency").getValue().toByteArray()))
                .build();

         this.paymentJpaRepository.save(payment);
    }

    public Optional<PaymentEntity> getPaymentById(String id) {
        Optional<PaymentEntity> payment = paymentJpaRepository.findById(id);
        return payment;
    }

    public void deletePaymentById(String id) {
        paymentJpaRepository.deleteById(id);
    }
}
