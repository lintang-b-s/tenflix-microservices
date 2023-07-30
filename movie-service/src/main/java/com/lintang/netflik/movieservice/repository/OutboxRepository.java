package com.lintang.netflik.movieservice.repository;

import com.lintang.netflik.movieservice.entity.OutboxEntity;
import org.springframework.data.repository.CrudRepository;

public interface OutboxRepository extends CrudRepository<OutboxEntity, Long> {
}
