package com.lintang.netflik.subscriptionservice.command.action;


import com.lintang.netflik.models.CreatePlanDto;
import com.lintang.netflik.subscriptionservice.entity.PlanEntity;
import com.lintang.netflik.subscriptionservice.repository.PlanJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class SubscriptionAction {


    @Autowired
    private PlanJpaRepository planJpaRepository;
    public PlanEntity createPlan(CreatePlanDto request) {
        PlanEntity createPlan = PlanEntity.builder()
                .name(request.getName())
                .price(BigDecimal.valueOf(request.getPrice())).description(request.getDescription())
                .activePeriod(request.getActivePeriod()).discountPrice(request.getDiscountPrice())

                .build();
        PlanEntity createdPlan = planJpaRepository.save(createPlan);
        return createdPlan;

    }
}

