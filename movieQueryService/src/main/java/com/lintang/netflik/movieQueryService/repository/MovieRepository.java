package com.lintang.netflik.movieQueryService.repository;

import com.lintang.netflik.movieQueryService.entity.MovieEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface MovieRepository extends MongoRepository<MovieEntity, Integer> {
    Optional<MovieEntity> findMovieEntitiesById(int movieId);
    Optional<MovieEntity> findById(int movieId);
    String deleteById(int movieId);

}
