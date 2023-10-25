package com.kafkastreams.movieservice.repository;

import com.kafkastreams.movieservice.entity.MovieEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<MovieEntity, Integer> {
    Optional<MovieEntity> findMovieEntitiesById(int movieId);
    Optional<MovieEntity> findById(int movieId);
    String deleteById(int movieId);

    Optional<MovieEntity> findByName(String name);

}
