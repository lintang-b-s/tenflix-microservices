package com.lintang.netflik.orderaggregatorservice.query.response;


import com.lintang.netflik.orderaggregatorservice.command.response.OrderDtoResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetOrdersResponse {
    List<OrderDtoResponse> orders;
}
