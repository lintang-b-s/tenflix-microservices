package com.lintang.netflik.orderservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_plan_m_view", schema = "public")
@Entity
@Getter
@Setter
public class PlanEntity {
    @Id
    private Long planId;
    private String name;
    private BigDecimal price;
    private String description;
    private int activePeriod;
    private int discountPrice;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlanEntity that = (PlanEntity) o;
        return Objects.equals(planId, that.planId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(planId);
    }
}
