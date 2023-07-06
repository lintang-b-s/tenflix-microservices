package com.lintang.netflik.orderservice.broker.message;

import com.lintang.netflik.orderservice.entity.OrderEntity;
import com.lintang.netflik.orderservice.entity.OrderStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CompleteOrderMessage {
    private OrderEntity order;
}
