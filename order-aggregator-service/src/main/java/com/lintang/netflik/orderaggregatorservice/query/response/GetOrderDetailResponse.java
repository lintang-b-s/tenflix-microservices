package com.lintang.netflik.orderaggregatorservice.query.response;


import com.lintang.netflik.orderaggregatorservice.command.response.OrderDtoResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetOrderDetailResponse {
    private SubscriptionDto subscriptionDto;
    private OrderDtoResponse orderDto;
    private PaymentDto paymentDto;
}
