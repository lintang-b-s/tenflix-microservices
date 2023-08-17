package com.lintang.netflik.orderaggregatorservice.query.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlanDto {

    private Long planId;
    private String name;
    private BigDecimal price;
    private String description;
    private int activePeriod;
    private int discountPrice;
}
