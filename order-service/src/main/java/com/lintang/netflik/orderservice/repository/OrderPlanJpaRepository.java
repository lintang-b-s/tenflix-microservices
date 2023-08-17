package com.lintang.netflik.orderservice.repository;

import com.lintang.netflik.orderservice.entity.OrderPlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderPlanJpaRepository extends JpaRepository<OrderPlanEntity, Long> {
}
