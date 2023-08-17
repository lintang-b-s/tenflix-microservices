package com.lintang.netflik.paymentservice.broker.message;


import com.lintang.netflik.paymentservice.entity.OrderStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentCanceledMessage {
    private OrderStatus orderStatus;
    private String grossAmunt;
    private String paymentId;
    private String failureMessages;
    private String orderId;
}
