package com.lintang.netflik.orderservice.broker.message;

import com.lintang.netflik.orderservice.entity.OrderStatus;
import javax.persistence.CascadeType;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class OrderMessage {
    private UUID id;
    private UUID userId;
    private Long price;

    private OrderStatus orderStatus;
    private String failureMessages;
    private String paymentId;
    private PlanMessage plan;

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
