package com.lintang.netflik.orderaggregatorservice.command.response;


import com.lintang.netflik.models.OrderDto;
import com.lintang.netflik.orderaggregatorservice.common.OrderStatus;
import com.lintang.netflik.orderaggregatorservice.entity.OrderEntity;
import com.lintang.netflik.orderaggregatorservice.entity.PlanEntity;
import com.lintang.netflik.orderaggregatorservice.entity.UserEntity;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
@Getter
@Setter
public class CreateOrderResponse {
    private String orderStatus;
    private String redirectUrl;
    private OrderDtoResponse order;


}
