package com.lintang.netflik.movieQueryService.repository;

import com.lintang.netflik.movieQueryService.entity.CreatorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CreatorRepository  extends JpaRepository<CreatorEntity, Integer> {

    Optional<CreatorEntity> findById(int creatorId);

    CreatorEntity save(CreatorEntity creatorEntity);
}
