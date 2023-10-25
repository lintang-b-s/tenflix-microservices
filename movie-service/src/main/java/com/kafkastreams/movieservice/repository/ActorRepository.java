package com.kafkastreams.movieservice.repository;

import com.kafkastreams.movieservice.entity.ActorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ActorRepository extends JpaRepository<ActorEntity, Integer> {
    Optional<ActorEntity> findById(int actorId);
    Optional<ActorEntity> findByName(String name);
}
