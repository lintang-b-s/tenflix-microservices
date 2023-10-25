package com.kafkastreams.movieservice.repository;

import com.kafkastreams.movieservice.api.request.AddMovieReq;
import com.kafkastreams.movieservice.entity.MovieEntity;

import java.util.Optional;


public interface MovieRepositoryExt {
    Optional<MovieEntity> insert(AddMovieReq newMovie);
}
