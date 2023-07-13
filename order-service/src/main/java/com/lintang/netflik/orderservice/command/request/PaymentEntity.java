package com.lintang.netflik.orderservice.command.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class PaymentEntity {

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
