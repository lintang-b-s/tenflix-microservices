package com.lintang.netflik.orderaggregatorservice.query.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CheckSubscriptionResponse {
    private SubscriptionDto subscriptionDto;
}
