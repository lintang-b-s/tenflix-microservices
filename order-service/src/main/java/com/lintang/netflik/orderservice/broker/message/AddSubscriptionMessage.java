package com.lintang.netflik.orderservice.broker.message;

import com.lintang.netflik.orderservice.entity.OrderPlanEntity;
import com.lintang.netflik.orderservice.entity.OrderStatus;
import javax.persistence.CascadeType;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToOne;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
@Data
public class AddSubscriptionMessage {
    private String orderId;
    private String userId;
    private Long price;
    private OrderStatus orderStatus;
    private String failureMessages;
    private String paymentId;
    private Long planId;
}
