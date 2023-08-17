package com.lintang.netflik.orderaggregatorservice.command.service;

import com.lintang.netflik.models.CreatePlanDto;
import com.lintang.netflik.models.CreatePlanGrpcRequest;

import com.lintang.netflik.models.PlanDto;
import com.lintang.netflik.models.SubscriptionServiceGrpc;
import com.lintang.netflik.orderaggregatorservice.command.request.CreatePlanRequest;
import com.lintang.netflik.orderaggregatorservice.command.response.CreatePlanResponse;
import com.lintang.netflik.orderaggregatorservice.entity.PlanEntity;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MatchingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class SubscriptionAggregateGrpcService {
    private static final Logger LOG = LoggerFactory.getLogger(SubscriptionAggregateGrpcService.class);

    @GrpcClient("subscription-service")
    private SubscriptionServiceGrpc.SubscriptionServiceBlockingStub subscriptionStub;

        public CreatePlanResponse createPlan(CreatePlanRequest createPlanRequest) {
            CreatePlanGrpcRequest createPlanGrpcRequest = CreatePlanGrpcRequest.newBuilder()
                    .setCreatePlanDto(CreatePlanDto.newBuilder()
                            .setName(createPlanRequest.getName()).setPrice(createPlanRequest.getPrice())
                            .setDescription(createPlanRequest.getDescription()).setActivePeriod(createPlanRequest.getActivePeriod())
                            .setDiscountPrice(createPlanRequest.getDiscountPrice())
                            .build())
                    .build();
            PlanDto plan = this.subscriptionStub.createPlan(createPlanGrpcRequest).getPlanDto();

            CreatePlanResponse planEntity = CreatePlanResponse.builder()
                    .planId((int) plan.getPlanId()).name(plan.getName()).price((int) plan.getPrice())
                    .description(plan.getDescription()).activePeriod(plan.getActivePeriod()).discountPrice(plan.getDiscountPrice())
                    .build();
            return planEntity;
        }

}
