package com.lintang.netflik.subscriptionservice.command.response;

import com.lintang.netflik.subscriptionservice.entity.PlanEntity;
import lombok.Builder;

@Builder
public class CreatePlanResponse {
    private PlanEntity plan;
}
