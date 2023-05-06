package com.lintang.netflik.movieQueryService.repository;

import com.lintang.netflik.movieQueryService.entity.GetAllMovies;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GetAllMoviesRepository extends JpaRepository<GetAllMovies, Integer> {

}
