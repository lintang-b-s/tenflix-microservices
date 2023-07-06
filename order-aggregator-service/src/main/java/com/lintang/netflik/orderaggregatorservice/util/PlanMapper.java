package com.lintang.netflik.orderaggregatorservice.util;

import com.lintang.netflik.models.PlanDto;
import com.lintang.netflik.orderaggregatorservice.entity.PlanEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PlanMapper {

    public PlanEntity planDtoToPlanEntity(PlanDto planDto) {
        PlanEntity plan = PlanEntity.builder().planId(planDto.getPlanId()).activePeriod(planDto.getActivePeriod())
                .description(planDto.getDescription()).discountPrice(planDto.getDiscountPrice()).price(BigDecimal.valueOf(planDto.getPrice())
                ).name(planDto.getName())
                .build();
        return plan;
    }
}
