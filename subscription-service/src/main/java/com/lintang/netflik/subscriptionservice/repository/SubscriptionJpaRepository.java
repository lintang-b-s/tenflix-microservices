package com.lintang.netflik.subscriptionservice.repository;

import com.lintang.netflik.subscriptionservice.entity.SubscriptionEntity;
import com.lintang.netflik.subscriptionservice.entity.SubscriptionStatus;
import org.checkerframework.checker.nullness.Opt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SubscriptionJpaRepository extends JpaRepository<SubscriptionEntity, Long> {


    List<SubscriptionEntity> findSubscriptionEntitiesByStatusAndUserId(SubscriptionStatus subscriptionStatus, UUID userId);
    Optional<SubscriptionEntity> findByOrderId(String orderId);
}
