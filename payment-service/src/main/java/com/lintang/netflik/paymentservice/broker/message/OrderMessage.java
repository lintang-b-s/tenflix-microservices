package com.lintang.netflik.paymentservice.broker.message;

import com.lintang.netflik.paymentservice.entity.OrderStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;


@Data
@Builder
public class OrderMessage {
    private UUID id;
    private UUID userId;
    private BigDecimal price;

    private OrderStatus orderStatus;
    private String failureMessages;
    private String paymentId;


    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
