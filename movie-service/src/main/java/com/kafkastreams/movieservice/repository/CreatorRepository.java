package com.kafkastreams.movieservice.repository;

import com.kafkastreams.movieservice.entity.CreatorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CreatorRepository  extends JpaRepository<CreatorEntity, Integer> {

    Optional<CreatorEntity> findById(int creatorId);
    Optional<CreatorEntity> findByName(String name);
}
