package com.lintang.netflik.orderaggregatorservice.query.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDto {
    private String id;
    private String status;
    private String grossAmount;
    private String transactionTime;
    private String transactionId;
    private String transactionStatus;
    private String paymentType;
    private String fraudStatus;
    private String bank;
    private String vaNumber;
    private String currency;
    private String orderId;

}
