package com.lintang.netflik.orderservice.command.action;

import com.lintang.netflik.models.OrderDto;
import com.lintang.netflik.orderservice.entity.OrderEntity;
import com.lintang.netflik.orderservice.entity.OrderPlanEntity;
import com.lintang.netflik.orderservice.entity.OrderStatus;
import com.lintang.netflik.orderservice.exception.ResourceNotFoundException;
import com.lintang.netflik.orderservice.repository.OrderJpaRepository;
import com.lintang.netflik.orderservice.repository.OrderPlanJpaRepository;
import com.lintang.netflik.orderservice.util.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class OrderAction {

    @Autowired
    private OrderJpaRepository orderJpaRepository;

    @Autowired
    private OrderPlanJpaRepository orderPlanJpaRepository;

    @Autowired
    private OrderMapper orderMapper;

    public OrderDto saveOrder(OrderDto order) {
        OrderPlanEntity orderPlanEntity = orderMapper.orderDtoPlanToEntity(order.getPlan());
        OrderEntity orderEntity  = orderMapper.orderDtoToEntity(order);
        orderPlanEntity.setOrder(orderEntity);
        orderEntity.setPlan(orderPlanEntity);
        OrderPlanEntity savedOrderPlan = orderPlanJpaRepository.save(orderPlanEntity);

        OrderEntity savedOrder = orderJpaRepository.save(orderEntity );

        OrderDto orderDto = orderMapper.orderEntityToDto(savedOrder, savedOrderPlan);
        return orderDto;
    }



    public Optional<OrderEntity> findOrderById(String id) {
        Optional<OrderEntity> order = orderJpaRepository.findById(UUID.fromString(id));
        return order;
    }


    public boolean userHasMoreThanOneOrder(String userId) {
        boolean isExists= orderJpaRepository.existsByUserIdAndOrderStatus(UUID.fromString(userId), OrderStatus.PENDING);
       return isExists;

    }
}
