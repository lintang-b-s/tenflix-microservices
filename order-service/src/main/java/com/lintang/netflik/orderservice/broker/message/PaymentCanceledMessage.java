package com.lintang.netflik.orderservice.broker.message;


import com.lintang.netflik.orderservice.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PaymentCanceledMessage {
    private OrderStatus orderStatus;
    private String grossAmunt;
    private String paymentId;
    private String failureMessages;
    private String orderId;
}
