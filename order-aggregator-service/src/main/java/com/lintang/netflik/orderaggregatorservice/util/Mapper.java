package com.lintang.netflik.orderaggregatorservice.util;


import com.lintang.netflik.models.*;
import com.lintang.netflik.orderaggregatorservice.command.request.CreateOrderRequest;
import com.lintang.netflik.orderaggregatorservice.entity.OrderEntity;
import com.lintang.netflik.orderaggregatorservice.entity.OrderPlanEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
public class Mapper {

    public OrderPlanEntity planDtoToOrderPlanEntity(PlanDto planDto) {
        OrderPlanEntity orderPlanEntity = new OrderPlanEntity();
        orderPlanEntity.setId(UUID.randomUUID());
        orderPlanEntity.setName(planDto.getName());
        orderPlanEntity.setDescription(planDto.getDescription());
        orderPlanEntity.setPrice(BigDecimal.valueOf(planDto.getPrice()));
        orderPlanEntity.setSubTotal(BigDecimal.valueOf(planDto.getPrice()));
        orderPlanEntity.setPlanId(planDto.getPlanId());
        return orderPlanEntity;
    }

    public OrderEntity createOrderReqToOrderEntity(CreateOrderRequest createOrderRequest
                                             ,PlanDto planDto,
                                             OrderPlanEntity orderPlanEntity      ) {
        OrderEntity createdOrder = new OrderEntity();
        createdOrder.setOrderStatus(com.lintang.netflik.orderaggregatorservice.common.OrderStatus.PENDING);
        createdOrder.setId(UUID.randomUUID());
        createdOrder.setUserId(UUID.fromString(createOrderRequest.getUserId()));
        createdOrder.setPrice(BigDecimal.valueOf(planDto.getPrice()));
        createdOrder.setPaymentId(UUID.randomUUID().toString());
        createdOrder.setFailureMessages("");
        createdOrder.setPlan(orderPlanEntity);
        return createdOrder;
    }

    public OrderPlanDto orderPlanEntityToOrderPlanDto(OrderPlanEntity orderPlanEntity) {
        OrderPlanDto orderPlanDto = OrderPlanDto.newBuilder().setId(orderPlanEntity.getId().toString())
               .setPlanId(orderPlanEntity.getPlanId()).setPrice(orderPlanEntity.getPrice().longValue())
                .setSubTotal(orderPlanEntity.getSubTotal().longValue()).build();

        return orderPlanDto;
    }

    public CreateOrderGrpcRequest createdOrderToCreateOrderGpcRequest(OrderEntity createdOrder ,PlanDto planDto
                                                                      , OrderPlanDto orderPlanDto) {
        CreateOrderGrpcRequest createOrderGrpcRequest = CreateOrderGrpcRequest.newBuilder().setOrder(
                        OrderDto.newBuilder().setId(createdOrder.getId().toString()).setUserId(createdOrder.getUserId().toString())
                                .setPrice(planDto.getPrice()).setOrderStatus(OrderStatus.PENDING)
                                .setPaymentId(createdOrder.getPaymentId()).setFailureMessages(createdOrder.getFailureMessages())
                                .setPlan(orderPlanDto).build())
                .build();
        return createOrderGrpcRequest;
    }

    public TransactionDetails createTransactionDetails(OrderEntity createdOrder, CustomerDetails customerDetails,
                                                       PlanDto planDto) {
        TransactionDetails transactionDetails = TransactionDetails.newBuilder()
                .setOrderId(createdOrder.getId().toString()).setGrossAmount(createdOrder.getPrice().longValue())
                .setCreditCard(true).setCustomerDetails(customerDetails).setItemDetails(
                        ItemDetails.newBuilder().setPlanId(planDto.getPlanId()).setPrice(planDto.getPrice())
                                .setName(planDto.getName()).build())
                .build();

        return transactionDetails;
    }
}
