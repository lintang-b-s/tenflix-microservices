package com.lintang.netflik.orderservice.broker.message;

import com.lintang.netflik.orderservice.entity.OrderStatus;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AddedSubscriptionMessage {
    private OrderStatus orderStatus;

    private String failureMessages;
    private String orderId;
}
