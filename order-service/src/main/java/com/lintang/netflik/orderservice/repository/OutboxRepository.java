package com.lintang.netflik.orderservice.repository;

import com.lintang.netflik.orderservice.entity.OutboxEntity;
import org.springframework.data.repository.CrudRepository;

public interface OutboxRepository extends CrudRepository<OutboxEntity, Long> {
}
