package com.lintang.netflik.paymentservice.repository;

import com.lintang.netflik.paymentservice.entity.OutboxEntity;
import org.springframework.data.repository.CrudRepository;

public interface OutboxRepository extends CrudRepository<OutboxEntity, Long> {
}
