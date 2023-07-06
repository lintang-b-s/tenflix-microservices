package com.lintang.netflik.orderservice.broker.message;

import com.lintang.netflik.orderservice.entity.OrderStatus;
import javax.persistence.CascadeType;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToOne;
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
