package com.lintang.netflik.orderservice.command.action;

import com.lintang.netflik.orderservice.entity.OrderEntity;
import com.lintang.netflik.orderservice.entity.OrderPlanEntity;
import com.lintang.netflik.orderservice.entity.OrderStatus;
import com.lintang.netflik.orderservice.repository.OrderJpaRepository;
import com.lintang.netflik.orderservice.repository.OrderPlanJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class OrderSagaAction {

    @Autowired
    private OrderJpaRepository orderJpaRepository;
    @Autowired
    private OrderPlanJpaRepository orderPlanJpaRepository;

    public OrderEntity updateOrderStatusAction(String orderId, OrderStatus orderStatus) {
        OrderEntity updatedOrder = orderJpaRepository.updateOrderStatusById(UUID.fromString(orderId),
                orderStatus.name());

        return updatedOrder;
    }



    public OrderEntity findById(String orderId) {
        OrderEntity order = orderJpaRepository.findById(UUID.fromString(orderId)).get();
        return order;
    }
}
