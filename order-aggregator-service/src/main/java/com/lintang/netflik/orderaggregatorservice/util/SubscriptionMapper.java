package com.lintang.netflik.orderaggregatorservice.util;


import com.google.protobuf.Timestamp;
import com.lintang.netflik.orderaggregatorservice.query.response.PlanDto;
import com.lintang.netflik.orderaggregatorservice.query.response.SubscriptionDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Component
public class SubscriptionMapper {
    public SubscriptionDto subscriptionDtoProtoToDto(com.lintang.netflik.models.SubscriptionDto protoDto) {
        Timestamp endSubcriptionDateProto = protoDto.getEndSubscriptionDate();
        LocalDateTime endDateJava = Instant.ofEpochSecond(endSubcriptionDateProto.getSeconds(), endSubcriptionDateProto.getNanos())
                .atOffset(ZoneOffset.UTC)
                .toLocalDateTime();
        SubscriptionDto subscriptionDto= SubscriptionDto.builder()
                .id(Long.valueOf(protoDto.getId()))
                .userId(UUID.fromString(protoDto.getUserId()))
                .endSubscriptionDate(endDateJava)
                .status(protoDto.getStatus())
                .plan(planDtoProtoToDto(protoDto.getPlan()))
                .build();

        return  subscriptionDto;
    }

    public PlanDto planDtoProtoToDto(com.lintang.netflik.models.PlanDto protoDto) {
        PlanDto planDto = PlanDto.builder()
                .planId(protoDto.getPlanId())
                .name(protoDto.getName()).price(BigDecimal.valueOf(protoDto.getPrice()))
                .description(protoDto.getDescription()).activePeriod(protoDto.getActivePeriod())
                .discountPrice(protoDto.getDiscountPrice())
                .build();
        return planDto;
    }
}
