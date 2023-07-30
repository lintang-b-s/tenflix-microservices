package com.lintang.netflik.movieservice.repository;

import com.lintang.netflik.movieservice.entity.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<TagEntity, Integer > {
    Optional<TagEntity> findById(int tagId);


}
