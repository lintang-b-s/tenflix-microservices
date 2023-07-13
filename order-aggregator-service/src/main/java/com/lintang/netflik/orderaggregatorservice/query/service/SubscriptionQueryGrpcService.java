package com.lintang.netflik.orderaggregatorservice.query.service;

import com.lintang.netflik.models.SubscriptionDto;
import com.lintang.netflik.orderaggregatorservice.query.action.SubscriptionQueryGrpcAction;
import com.lintang.netflik.orderaggregatorservice.query.response.CheckSubscriptionResponse;
import com.lintang.netflik.orderaggregatorservice.util.SubscriptionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionQueryGrpcService {

    @Autowired
    private SubscriptionQueryGrpcAction subscriptionQueryGrpcAction;

    @Autowired
    private SubscriptionMapper subscriptionMapper;

    public CheckSubscriptionResponse checkUserSubscription(String userId) {
        SubscriptionDto subscriptionDto = this.subscriptionQueryGrpcAction.getUserCurrentSubscription(userId).getSubscriptionDto();
        com.lintang.netflik.orderaggregatorservice.query.response.SubscriptionDto subscriptionResponse=
                subscriptionMapper.subscriptionDtoProtoToDto(subscriptionDto);

        CheckSubscriptionResponse checkSubscriptionResponse = CheckSubscriptionResponse.builder()
                .subscriptionDto(subscriptionResponse)
                .build();

        return checkSubscriptionResponse;
    }
}
