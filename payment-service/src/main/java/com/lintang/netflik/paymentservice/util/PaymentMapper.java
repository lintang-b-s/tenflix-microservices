package com.lintang.netflik.paymentservice.util;


import com.lintang.netflik.models.PaymentDetail;
import com.lintang.netflik.paymentservice.entity.PaymentEntity;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {

    public PaymentDetail paymentEntityToPaymentDetail(PaymentEntity paymentEntity) {
        PaymentDetail paymentDetail = PaymentDetail.newBuilder()
                .setId(paymentEntity.getId()).setStatus(paymentEntity.getStatus())
                .setGrossAmount(paymentEntity.getGrossAmount()).setTransactionTime(paymentEntity.getTransactionTime())
                .setTransactionId(paymentEntity.getTransactionId()).setTransactionStatus(paymentEntity.getTransactionStatus())
                .setPaymentType(paymentEntity.getPaymentType()).setFraudStatus(paymentEntity.getFraudStatus())
                .setBank(paymentEntity.getBank()).setVaNumber(paymentEntity.getVaNumber()).setCurrency(paymentEntity.getCurrency())
                .setOrderId(paymentEntity.getOrderId())
                .build();

        return paymentDetail;
    }
}
