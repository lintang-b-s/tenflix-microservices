package com.lintang.netflik.movieQueryService.repository;

import com.lintang.netflik.movieQueryService.entity.ActorEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import javax.swing.text.html.Option;
import java.util.Optional;


public interface ActorRepository extends MongoRepository<ActorEntity, Integer> {
    Optional<ActorEntity> findById(int actorId);

    ActorEntity  save(ActorEntity entity);
}