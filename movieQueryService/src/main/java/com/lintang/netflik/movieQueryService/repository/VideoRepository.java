package com.lintang.netflik.movieQueryService.repository;

import com.lintang.netflik.movieQueryService.entity.VideoEntity;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface VideoRepository extends MongoRepository<VideoEntity, Integer> {
    @Query("{ 'movieId' : ?0}")
    List<VideoEntity> findByMovieId( int movieId);

    Optional<VideoEntity> getVideoEntitiesByMovie_IdAndId(int movieId, int videoId);

    String deleteVideoEntitiesByMovie_IdAndId(int movieId, int videoId);

    @Query(("{ '_id' :  ?0}"))
    Optional<VideoEntity> findById(int videoId);

    VideoEntity save(VideoEntity videoEntity);



}

