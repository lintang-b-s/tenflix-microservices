package com.kafkastreams.movieservice.repository;

import com.kafkastreams.movieservice.entity.OutboxEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OutboxRepository extends CrudRepository<OutboxEntity, Long> {
}
