package com.lintang.netflik.movieservice.repository;

import com.lintang.netflik.movieservice.api.request.AddMovieReq;
import com.lintang.netflik.movieservice.entity.MovieEntity;

import java.util.Optional;

public interface MovieRepositoryExt {
    Optional<MovieEntity> insert(AddMovieReq newMovie);
}
