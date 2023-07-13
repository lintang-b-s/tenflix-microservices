package com.lintang.netflik.subscriptionservice.command.action;

import com.lintang.netflik.subscriptionservice.entity.PlanEntity;
import com.lintang.netflik.subscriptionservice.entity.SubscriptionEntity;
import com.lintang.netflik.subscriptionservice.entity.SubscriptionStatus;
import com.lintang.netflik.subscriptionservice.repository.SubscriptionJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class SubscriptionSagaAction {

    @Autowired
    private SubscriptionJpaRepository subscriptionJpaRepository;


 /*
    Add subscription to User
    */
    public SubscriptionEntity addSubscriptionToUserAction(String userId , PlanEntity plan , String orderId) {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, plan.getActivePeriod());
        Date endSubscription = cal.getTime();
        SubscriptionEntity newSubscription = SubscriptionEntity.builder()
                .userId(UUID.fromString(userId))
                .plan(plan)
                .endSubscriptionDate(endSubscription)
                .status(SubscriptionStatus.ACTIVE)
                .orderId(orderId) // order id disimpen biar pas query history order  bisa tau order A buat subscription A
                .build();
        return subscriptionJpaRepository.save(newSubscription);
    }

   /*
        get active user subscription
    */
    public List<SubscriptionEntity> findActiveSubscription(String userId) {
        List<SubscriptionEntity> subscriptionEntity = this.subscriptionJpaRepository.findSubscriptionEntitiesByStatusAndUserId(
                SubscriptionStatus.ACTIVE, UUID.fromString(userId)
        );
        return subscriptionEntity;
    }
}
