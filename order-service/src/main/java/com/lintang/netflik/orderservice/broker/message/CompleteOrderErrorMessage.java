package com.lintang.netflik.orderservice.broker.message;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CompleteOrderErrorMessage {
    private String orderId;
    private String userId;
    private String planId;
}
