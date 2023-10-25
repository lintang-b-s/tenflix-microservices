package com.kafkastreams.movieservice.repository;

import com.kafkastreams.movieservice.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Integer> {
    Optional<CategoryEntity> findById(int categoryId);
    Optional<CategoryEntity> findByName(String name);
}
