package com.lintang.netflik.movieQueryService.query.service;


import com.lintang.netflik.movieQueryService.command.action.MovieCommandAction;
import com.lintang.netflik.movieQueryService.entity.MovieEntity;
import com.lintang.netflik.movieQueryService.query.action.MovieQueryAction;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Service
public class MovieQueryService {
    private MovieQueryAction movieQueryAction;
    
    @Autowired
    public MovieQueryService(MovieQueryAction  movieQueryAction) {
        this.movieQueryAction = movieQueryAction;
    }

    public MovieEntity getMovieById(@NotNull @Valid int movieId, String userId) {
        MovieEntity entity = movieQueryAction.getMovieById(movieId, userId);
        return entity;
    }

    public List<MovieEntity> getAllMovie(String userId) {
        List<MovieEntity> movies = movieQueryAction.getAllMovie(userId);

        return movies;
    }

}
