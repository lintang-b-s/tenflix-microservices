package com.lintang.netflik.orderservice.broker.message;

import com.lintang.netflik.orderservice.entity.OrderStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateFailMessage {
    private OrderStatus orderStatus;
    private String grossAmunt;
    private String paymentId;
    private String failureMessages;
    private String orderId;
}
