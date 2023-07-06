package com.lintang.netflik.orderaggregatorservice.command.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
public class OrderPlanDtoResponse {
    private UUID id;

    private String name;
    private String description;


    private Long planId;
    private Long price;
    private Long subTotal;
}
