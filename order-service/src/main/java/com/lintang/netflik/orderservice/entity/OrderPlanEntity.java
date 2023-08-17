package com.lintang.netflik.orderservice.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_plans", schema = "public")
@Entity
public class OrderPlanEntity {
    @Id
    private UUID id;


    private Long planId;
    private Long price;
    private Long subTotal;
    @OneToOne(mappedBy = "plan")
    private OrderEntity order;




    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderPlanEntity that = (OrderPlanEntity) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
