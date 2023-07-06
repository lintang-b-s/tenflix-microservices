package com.lintang.netflik.orderaggregatorservice.command.response;

import com.lintang.netflik.orderaggregatorservice.entity.PlanEntity;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Data
@Getter
@Setter
@Builder
public class CreatePlanResponse {
    private int planId;
    private String name;
    private int price;
    private String description;
    private int activePeriod;
    private int discountPrice;
}
