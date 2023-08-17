package com.lintang.netflik.subscriptionservice.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name= "plans")
@Entity
@Builder
public class PlanEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long planId;
    private String name;
    private BigDecimal price;
    private String description;
    private int activePeriod;
    private int discountPrice;

    @OneToMany(mappedBy = "plan",  fetch = FetchType.EAGER)
    private Set<SubscriptionEntity> subscriptions = new HashSet<>();


    public Set<SubscriptionEntity> getSubscription() {
        return subscriptions;
    }

    public PlanEntity setSubscription(Set<SubscriptionEntity> subscriptions) {
        this.subscriptions = subscriptions;
        return this;
    }



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
