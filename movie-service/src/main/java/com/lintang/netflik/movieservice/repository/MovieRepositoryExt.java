package com.lintang.netflik.movieservice.repository;

import com.lintang.netflik.movieservice.dto.AddMovieReq;
import com.lintang.netflik.movieservice.entity.MovieEntity;

import java.util.Optional;

public interface MovieRepositoryExt {
    Optional<MovieEntity> insert(AddMovieReq newMovie);
}
