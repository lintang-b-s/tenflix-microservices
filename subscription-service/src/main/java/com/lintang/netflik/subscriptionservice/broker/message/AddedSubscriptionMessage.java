package com.lintang.netflik.subscriptionservice.broker.message;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AddedSubscriptionMessage {
    private OrderStatus orderStatus;

    private String failureMessages;
    private String orderId;
}
