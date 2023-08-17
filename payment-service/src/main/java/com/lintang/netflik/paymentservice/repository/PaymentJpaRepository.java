package com.lintang.netflik.paymentservice.repository;

import com.lintang.netflik.paymentservice.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PaymentJpaRepository extends JpaRepository<PaymentEntity, String> {
    Optional<PaymentEntity> findByOrderId(String orderId);

}
