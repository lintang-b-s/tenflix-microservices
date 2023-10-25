package com.kafkastreams.movieservice.repository;

import com.kafkastreams.movieservice.entity.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<TagEntity, Integer > {
    Optional<TagEntity> findById(int tagId);

    Optional<TagEntity> findByName(String name);
}
