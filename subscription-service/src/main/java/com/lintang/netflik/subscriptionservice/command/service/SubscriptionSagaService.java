package com.lintang.netflik.subscriptionservice.command.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lintang.netflik.subscriptionservice.broker.message.AddSubscriptionErrorMessage;
import com.lintang.netflik.subscriptionservice.broker.message.AddSubscriptionMessage;
import com.lintang.netflik.subscriptionservice.broker.message.AddedSubscriptionMessage;
import com.lintang.netflik.subscriptionservice.broker.message.OrderStatus;
import com.lintang.netflik.subscriptionservice.command.action.PlanSagaAction;
import com.lintang.netflik.subscriptionservice.command.action.SubscriptionOutboxAction;
import com.lintang.netflik.subscriptionservice.command.action.SubscriptionSagaAction;
import com.lintang.netflik.subscriptionservice.entity.OutboxEventType;
import com.lintang.netflik.subscriptionservice.entity.PlanEntity;
import com.lintang.netflik.subscriptionservice.entity.SagaStatus;
import com.lintang.netflik.subscriptionservice.entity.SubscriptionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SubscriptionSagaService {

    @Autowired
    private SubscriptionSagaAction subscriptionSagaAction;
    @Autowired
    private PlanSagaAction planSagaAction;
    @Autowired
    private SubscriptionOutboxAction outboxAction;

    @Transactional
    public void addSubscriptionToUser(AddSubscriptionMessage addSubscriptionMessage)
            throws JsonProcessingException
    {
        Long planId = Long.valueOf(addSubscriptionMessage.getPlanId());
        UUID userId = UUID.fromString(addSubscriptionMessage.getUserId());

        Optional<PlanEntity> plan = null;
        // get plan. If not exists , compensating previous transaction in order & payment service

         plan = planSagaAction.findById(planId);
        List<SubscriptionEntity> activeUserSubscription = subscriptionSagaAction.findActiveSubscription(userId.toString());

        if (!plan.isPresent()) {
             AddSubscriptionErrorMessage addSubscriptionErrorMessage = AddSubscriptionErrorMessage.builder()
                     .errorMessage("Cannot find planId " + planId ).grossAmount(addSubscriptionMessage.getPrice().toString())
                     .orderId(addSubscriptionMessage.getOrderId())
                     .build();
             var subscriptionOutbox = outboxAction.insertOutbox(
                     "subscription.response",
                     addSubscriptionErrorMessage.getOrderId(),
                     OutboxEventType.SUBSCRIPTION_ERROR,
                     addSubscriptionErrorMessage, SagaStatus.COMPENSATING
             );
             outboxAction.deleteOutbox(subscriptionOutbox);
             return;
         }

//        user gak punya subscription yg aktif (juga dicek pas createorder)
        if ( activeUserSubscription.size() == 0)  {
            SubscriptionEntity subscriptionEntity = subscriptionSagaAction.addSubscriptionToUserAction(userId.toString(), plan.get(), addSubscriptionMessage.getOrderId());
            AddedSubscriptionMessage addedSubscriptionMessage = AddedSubscriptionMessage
                    .builder().failureMessages("").orderStatus(OrderStatus.PAID)
                    .orderId(addSubscriptionMessage.getOrderId())
                    .build();
            var subscriptionOutbox = outboxAction.insertOutbox(
                    "subscription.response",
                    addSubscriptionMessage.getOrderId(),
                    OutboxEventType.ADDED_SUBSCRIPTION,
                    addedSubscriptionMessage, SagaStatus.PROCESSING
            );
            outboxAction.deleteOutbox(subscriptionOutbox);
        }

    }

}
