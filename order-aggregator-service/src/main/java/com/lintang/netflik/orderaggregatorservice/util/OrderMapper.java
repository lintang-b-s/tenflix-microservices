package com.lintang.netflik.orderaggregatorservice.util;

import com.lintang.netflik.models.OrderDto;
import com.lintang.netflik.models.OrderPlanDto;
import com.lintang.netflik.orderaggregatorservice.command.response.OrderDtoResponse;
import com.lintang.netflik.orderaggregatorservice.command.response.OrderPlanDtoResponse;
import com.lintang.netflik.orderaggregatorservice.entity.OrderEntity;
import com.lintang.netflik.orderaggregatorservice.entity.OrderPlanEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class OrderMapper {

    public OrderDtoResponse orderDtoToOrderResponse(OrderDto orderDto) {
        return OrderDtoResponse.builder().id(orderDto.getId())
                .userId(orderDto.getUserId()).price(orderDto.getPrice())
                .orderStatus(orderDto.getOrderStatus().name()).paymentId(orderDto.getPaymentId())
                .failureMessages(orderDto.getFailureMessages()).plan(orderPlanDtoToResponse(orderDto.getPlan()))
                .build();
    }

    public OrderPlanDtoResponse orderPlanDtoToResponse(OrderPlanDto orderPlanDto) {
        return OrderPlanDtoResponse.builder().id(UUID.fromString(orderPlanDto.getId()))
               .planId(orderPlanDto.getPlanId())
                .price(orderPlanDto.getPrice()).subTotal(orderPlanDto.getSubTotal())
                .build();
    }


}
