package com.lintang.netflik.orderservice.broker.message;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddSubscriptionErrorMessage {
    private String orderId;
    private String grossAmount;
    private String errorMessage;

    public boolean isValid() {
        return orderId != null && errorMessage != null;
    }
}
