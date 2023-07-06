package com.lintang.netflik.movieservice.repository;

import com.lintang.netflik.movieservice.entity.MovieEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MovieRepository extends JpaRepository<MovieEntity, Integer> {
    Optional<MovieEntity> findMovieEntitiesById(int movieId);
    Optional<MovieEntity> findById(int movieId);
    String deleteById(int movieId);

}
