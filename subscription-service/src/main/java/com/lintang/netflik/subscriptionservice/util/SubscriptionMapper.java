package com.lintang.netflik.subscriptionservice.util;

import com.google.protobuf.Timestamp;
import com.lintang.netflik.models.PlanDto;
import com.lintang.netflik.models.SubscriptionDto;
import com.lintang.netflik.subscriptionservice.entity.PlanEntity;
import com.lintang.netflik.subscriptionservice.entity.SubscriptionEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;

@Component
public class SubscriptionMapper {

    public SubscriptionDto subscriptionEntityToProtoDto(SubscriptionEntity subscriptionEntity) {

        LocalDateTime endDateEntity =  subscriptionEntity.getEndSubscriptionDate().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        Timestamp endDateProto = Timestamp.newBuilder()
                .setSeconds(endDateEntity.toEpochSecond(ZoneOffset.UTC))
                .setNanos(endDateEntity.getNano())
                .build();

        SubscriptionDto subscriptionDto =
                SubscriptionDto.newBuilder().setId(subscriptionEntity.getId().intValue())
                        .setUserId(subscriptionEntity.getUserId().toString())
                        .setStatus(subscriptionEntity.getStatus().name())
                        .setEndSubscriptionDate(endDateProto)
                        .setPlan(planEntityToProtoDto(subscriptionEntity.getPlan()))
                        .build();

        return subscriptionDto;
    }

    public PlanDto planEntityToProtoDto(PlanEntity plan) {
        PlanDto planDto = PlanDto.newBuilder().setPlanId(plan.getPlanId())
                .setName(plan.getName()).setPrice(plan.getPrice().intValue())
                .setDescription(plan.getDescription()).setActivePeriod(plan.getActivePeriod())
                .setDiscountPrice(plan.getDiscountPrice())
                .build();

        return planDto;
    }
}
