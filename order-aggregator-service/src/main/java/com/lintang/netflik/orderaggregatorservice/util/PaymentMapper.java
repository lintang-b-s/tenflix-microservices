package com.lintang.netflik.orderaggregatorservice.util;


import com.lintang.netflik.models.PaymentDetail;
import com.lintang.netflik.orderaggregatorservice.query.response.PaymentDto;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {

    public PaymentDto paymentProtoToDto(PaymentDetail paymentDetail) {
        PaymentDto paymentDto =
                PaymentDto.builder().id(paymentDetail.getId()).status(paymentDetail.getStatus())
                        .grossAmount(paymentDetail.getGrossAmount()).transactionTime(paymentDetail.getTransactionTime())
                        .transactionId(paymentDetail.getTransactionId()).transactionStatus(paymentDetail.getTransactionStatus())
                        .paymentType(paymentDetail.getPaymentType()).fraudStatus(paymentDetail.getFraudStatus())
                        .bank(paymentDetail.getBank()).vaNumber(paymentDetail.getVaNumber())
                        .currency(paymentDetail.getCurrency()).orderId(paymentDetail.getOrderId())
                        .build();

        return paymentDto;
    }
}
