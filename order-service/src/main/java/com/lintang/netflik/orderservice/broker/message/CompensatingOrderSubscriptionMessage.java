package com.lintang.netflik.orderservice.broker.message;


import com.lintang.netflik.orderservice.entity.OrderEntity;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CompensatingOrderSubscriptionMessage {
    private OrderMessage order;

}
