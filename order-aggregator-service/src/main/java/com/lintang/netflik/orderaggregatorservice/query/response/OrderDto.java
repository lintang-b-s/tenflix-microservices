package com.lintang.netflik.orderaggregatorservice.query.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {

    private String id;
    private String userId;
    private Long price;
    private String orderStatus;
    private String failureMessages;
    private String paymentId;
    private PlanDto planDto;

}

