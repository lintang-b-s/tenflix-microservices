package com.lintang.netflik.orderservice.util;


import com.lintang.netflik.orderservice.broker.message.OrderMessage;
import com.lintang.netflik.orderservice.broker.message.PlanMessage;
import com.lintang.netflik.orderservice.entity.OrderEntity;
import com.lintang.netflik.orderservice.entity.OrderPlanEntity;
import com.lintang.netflik.orderservice.entity.PlanEntity;
import org.springframework.stereotype.Component;

@Component
public class OrderMessageMapper {

    public OrderMessage orderEntityToOrderMessage(OrderEntity orderEntity) {
        OrderMessage orderMessage = OrderMessage.builder().id(orderEntity.getId())
                .userId(orderEntity.getUserId()).price(orderEntity.getPrice())
                .orderStatus(orderEntity.getOrderStatus()).failureMessages(orderEntity.getFailureMessages())
                .paymentId(orderEntity.getPaymentId()).plan(planEntityToPlanMessage(orderEntity.getPlan()))
                .build();
        return orderMessage;
    }

    public PlanMessage planEntityToPlanMessage(OrderPlanEntity planEntity) {
        PlanMessage planMessage = PlanMessage.builder().planId(planEntity.getPlanId())
                .price(planEntity.getPrice().longValue()).subTotal(planEntity.getPrice().longValue())

                .build();
        return planMessage;
    }
}
