package com.lintang.netflik.movieQueryService.repository;

import com.lintang.netflik.movieQueryService.entity.ViewEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ViewRepository extends MongoRepository<ViewEntity, Integer> {
    Optional<ViewEntity> findByVideoIdAndUserId(int videoId, String userId);
}
