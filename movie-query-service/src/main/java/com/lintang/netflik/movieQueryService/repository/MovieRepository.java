package com.lintang.netflik.movieQueryService.repository;

import com.lintang.netflik.movieQueryService.entity.MovieEntity;
import com.lintang.netflik.movieQueryService.entity.TagEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;


import java.util.Optional;
import java.util.Set;

public interface MovieRepository extends MongoRepository<MovieEntity, Integer> {
    Optional<MovieEntity> findMovieEntitiesById(int movieId);
    Optional<MovieEntity> findById(int movieId);
    String deleteById(int movieId);





}
