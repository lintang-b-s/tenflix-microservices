package com.lintang.netflik.subscriptionservice.entity;


import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Table(name = "subscriptions")
@Entity
@Builder
public class SubscriptionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID userId;

    @Temporal(TemporalType.TIMESTAMP)
    private Date endSubscriptionDate;

    @Enumerated(EnumType.STRING)
    private SubscriptionStatus status;

    private String orderId;

    @ManyToOne( fetch = FetchType.EAGER)
    @JoinColumn(name = "plan_id", referencedColumnName = "planId")
    private PlanEntity plan;

}
