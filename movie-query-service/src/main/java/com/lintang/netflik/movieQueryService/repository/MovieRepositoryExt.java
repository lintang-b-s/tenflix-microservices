package com.lintang.netflik.movieQueryService.repository;

import com.lintang.netflik.movieQueryService.dto.AddMovieReq;
import com.lintang.netflik.movieQueryService.entity.MovieEntity;

import java.util.Optional;

public interface MovieRepositoryExt {
    Optional<MovieEntity> insert(AddMovieReq newMovie);
}
