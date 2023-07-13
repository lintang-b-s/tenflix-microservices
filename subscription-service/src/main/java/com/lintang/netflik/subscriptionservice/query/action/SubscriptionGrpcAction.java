package com.lintang.netflik.subscriptionservice.query.action;

import com.lintang.netflik.models.GetPlanGrpcRequest;
import com.lintang.netflik.models.PlanDto;
import com.lintang.netflik.subscriptionservice.entity.PlanEntity;
import com.lintang.netflik.subscriptionservice.entity.SubscriptionEntity;
import com.lintang.netflik.subscriptionservice.entity.SubscriptionStatus;
import com.lintang.netflik.subscriptionservice.repository.PlanJpaRepository;
import com.lintang.netflik.subscriptionservice.repository.SubscriptionJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class SubscriptionGrpcAction   {

    @Autowired
    private PlanJpaRepository planJpaRepository;

    @Autowired
    private SubscriptionJpaRepository subscriptionJpaRepository;

    public Optional<PlanEntity> getPlanFromDb(GetPlanGrpcRequest getPlanGrpcRequest) {
        return this.planJpaRepository.findById(getPlanGrpcRequest.getCreateOrder().getPlanId());
    }

    public List<SubscriptionEntity> getActiveUserSubscription(String userId) {
        List<SubscriptionEntity> subscriptionEntities = this.subscriptionJpaRepository.
                findSubscriptionEntitiesByStatusAndUserId(SubscriptionStatus.ACTIVE, UUID.fromString(userId));

        return subscriptionEntities;

    }

    public Optional<SubscriptionEntity> getSubcriptionByOrderId(String orderId ) {
        Optional<SubscriptionEntity> subscriptionEntity = this.subscriptionJpaRepository.findByOrderId(orderId);

        return subscriptionEntity;
    }



}
