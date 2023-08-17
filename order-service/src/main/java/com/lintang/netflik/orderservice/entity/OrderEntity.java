package com.lintang.netflik.orderservice.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "orders", schema = "public")
@Entity
public class OrderEntity {
    @Id
    private UUID id;
    private UUID userId;
    private Long price;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    private String failureMessages;
    private String paymentId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "plan_id", referencedColumnName = "id")
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
