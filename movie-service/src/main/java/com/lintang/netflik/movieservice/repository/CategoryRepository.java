package com.lintang.netflik.movieservice.repository;

import com.lintang.netflik.movieservice.entity.CategoryEntity;
import com.lintang.netflik.movieservice.entity.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Integer> {
    Optional<CategoryEntity> findById(int categoryId);

}
