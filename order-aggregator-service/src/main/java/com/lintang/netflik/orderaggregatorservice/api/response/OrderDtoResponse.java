package com.lintang.netflik.orderaggregatorservice.command.response;

import com.lintang.netflik.models.OrderPlanDto;
import com.lintang.netflik.orderaggregatorservice.common.OrderStatus;
import com.lintang.netflik.orderaggregatorservice.entity.OrderPlanEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class OrderDtoResponse {
    private String id;
    private String userId;
    private Long price;
    private String orderStatus;
    private String paymentId;
    private String failureMessages;
    private OrderPlanDtoResponse plan;
}
