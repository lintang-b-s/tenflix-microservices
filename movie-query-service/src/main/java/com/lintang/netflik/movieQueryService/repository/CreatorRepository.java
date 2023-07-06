package com.lintang.netflik.movieQueryService.repository;

import com.lintang.netflik.movieQueryService.entity.CreatorEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CreatorRepository  extends MongoRepository<CreatorEntity, Integer> {

    Optional<CreatorEntity> findById(int creatorId);

    CreatorEntity save(CreatorEntity creatorEntity);
}
