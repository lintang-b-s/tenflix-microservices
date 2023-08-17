package com.lintang.netflik.subscriptionservice.broker.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class AddSubscriptionMessage {
    private String orderId;
    private String userId;
    private BigDecimal price;
    private OrderStatus orderStatus;
    private String failureMessages;
    private String paymentId;
    private String planId;
}
