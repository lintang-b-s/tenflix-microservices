package com.lintang.netflik.subscriptionservice.command.action;

import com.lintang.netflik.subscriptionservice.entity.PlanEntity;
import com.lintang.netflik.subscriptionservice.entity.SubscriptionEntity;
import com.lintang.netflik.subscriptionservice.repository.SubscriptionJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Component
public class SubscriptionSagaAction {

    @Autowired
    private SubscriptionJpaRepository subscriptionJpaRepository;

    public SubscriptionEntity addSubscriptionToUserAction(String userId , PlanEntity plan) {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, plan.getActivePeriod());
        Date endSubscription = cal.getTime();
        SubscriptionEntity newSubscription = SubscriptionEntity.builder()
                .userId(UUID.fromString(userId))
                .plan(plan)
                .endSubscriptionDate(endSubscription)
                .build();
        return subscriptionJpaRepository.save(newSubscription);
    }
}
