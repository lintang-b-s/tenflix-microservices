package com.lintang.netflik.paymentservice.broker.message;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CompensatingOrderSubscriptionMessage {
    private OrderMessage order;

}
