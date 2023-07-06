package com.lintang.netflik.orderaggregatorservice.entity;

import com.lintang.netflik.orderaggregatorservice.common.OrderStatus;
import lombok.*;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderEntity {

    private UUID id;
    private UUID userId;
    private BigDecimal price;
    private OrderStatus orderStatus;
    private String paymentId;
    private String failureMessages;
    private OrderPlanEntity plan;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderEntity that = (OrderEntity) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
