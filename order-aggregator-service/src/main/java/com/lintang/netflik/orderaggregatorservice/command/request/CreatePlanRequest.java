package com.lintang.netflik.orderaggregatorservice.command.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreatePlanRequest {
    private String name;
    private long price;
    private String description;
    private int activePeriod;
    private int discountPrice;
}
