package com.lintang.netflik.orderservice.broker.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
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
