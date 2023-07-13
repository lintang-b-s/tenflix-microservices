package com.lintang.netflik.orderservice.broker.message;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CompensatingOrderSubscriptionMessage {
    private OrderMessage order;

}
