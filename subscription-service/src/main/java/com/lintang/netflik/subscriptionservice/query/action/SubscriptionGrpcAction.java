package com.lintang.netflik.subscriptionservice.query.action;

import com.lintang.netflik.models.GetPlanGrpcRequest;
import com.lintang.netflik.models.PlanDto;
import com.lintang.netflik.subscriptionservice.entity.PlanEntity;
import com.lintang.netflik.subscriptionservice.repository.PlanJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SubscriptionGrpcAction   {

    @Autowired
    private PlanJpaRepository planJpaRepository;

    public Optional<PlanEntity> getPlanFromDb(GetPlanGrpcRequest getPlanGrpcRequest) {
        return this.planJpaRepository.findById(getPlanGrpcRequest.getCreateOrder().getPlanId());
    }
}
