package com.lintang.netflik.orderservice.util;

import com.lintang.netflik.models.OrderDto;
import com.lintang.netflik.models.OrderPlanDto;
import com.lintang.netflik.orderservice.entity.OrderEntity;
import com.lintang.netflik.orderservice.entity.OrderPlanEntity;
import com.lintang.netflik.orderservice.entity.OrderStatus;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
public class OrderMapper {

    public OrderEntity orderDtoToEntity(OrderDto orderDto) {
        OrderEntity orderEntity = OrderEntity.builder().id(UUID.randomUUID())
                .userId(UUID.fromString(orderDto.getUserId())).price(orderDto.getPrice())
                .orderStatus(OrderStatus.PENDING).failureMessages(orderDto.getFailureMessages())
                .paymentId(orderDto.getPaymentId()).build();
        return orderEntity;
    }

    public OrderPlanEntity orderDtoPlanToEntity(OrderPlanDto orderPlanDto ) {
        OrderPlanEntity orderPlanEntity= OrderPlanEntity.builder().id(UUID.randomUUID()).planId(orderPlanDto.getPlanId())
                .price(orderPlanDto.getPrice()).subTotal(orderPlanDto.getSubTotal())
                .build();
        return orderPlanEntity;
    }

    public OrderDto orderEntityToDto(OrderEntity order, OrderPlanEntity orderPlanEntity) {
        return OrderDto.newBuilder().setId(order.getId().toString()).setUserId(order.getUserId().toString())
                .setPrice(order.getPrice().intValue()).setOrderStatus(orderStatusToGrpc(order.getOrderStatus()))
                .setFailureMessages(order.getFailureMessages()).setPlan(orderPlanEntitytoDto(orderPlanEntity))
                .build();
    }
    private OrderPlanDto orderPlanEntitytoDto(OrderPlanEntity orderPlanEntity) {
        return OrderPlanDto.newBuilder().setId(orderPlanEntity.getId().toString())
                .setPlanId(orderPlanEntity.getPlanId()).setPrice(orderPlanEntity.getPrice().longValue())
                .setSubTotal(orderPlanEntity.getSubTotal().longValue())
                .build();
    }

    public com.lintang.netflik.models.OrderStatus orderStatusToGrpc(OrderStatus orderStatus) {
        com.lintang.netflik.models.OrderStatus returnVal = null;
        switch (orderStatus) {
            case PENDING:
                returnVal =  com.lintang.netflik.models.OrderStatus.PENDING;
                break;
            case CANCELLED:
                returnVal =  com.lintang.netflik.models.OrderStatus.CANCELLED;
                break;
            case  PAID:
                 returnVal =  com.lintang.netflik.models.OrderStatus.PAID;
                break;
            case COMPLETED:
                 returnVal =  com.lintang.netflik.models.OrderStatus.COMPLETED;
                break;
            default:
                 returnVal =  com.lintang.netflik.models.OrderStatus.PENDING;
                break;

        }
        return returnVal;
    }

    public OrderPlanDto orderPlanToDto(OrderPlanEntity orderPlanEntity) {
        return OrderPlanDto.newBuilder()
                .setId(orderPlanEntity.getId().toString()).setPlanId(Long.valueOf(orderPlanEntity.getPlanId()))
                .setPrice(orderPlanEntity.getPrice().longValue())
                .setSubTotal(orderPlanEntity.getSubTotal().longValue())
                .build();
    }
}
