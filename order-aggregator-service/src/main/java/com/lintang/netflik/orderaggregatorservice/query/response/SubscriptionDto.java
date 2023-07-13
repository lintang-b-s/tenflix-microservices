package com.lintang.netflik.orderaggregatorservice.query.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionDto {
    private Long id;
    private UUID userId;
    private LocalDateTime endSubscriptionDate;
    private String status;

    private PlanDto plan;

}
