package com.lintang.netflik.subscriptionservice.repository;

import com.lintang.netflik.subscriptionservice.entity.OutboxEntity;
import org.springframework.data.repository.CrudRepository;

public interface OutboxRepository extends CrudRepository<OutboxEntity, Long> {
}
