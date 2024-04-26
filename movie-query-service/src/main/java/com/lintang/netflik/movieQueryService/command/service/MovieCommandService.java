package com.lintang.netflik.movieQueryService.command.service;

import com.lintang.netflik.movieQueryService.broker.message.AddMovieMessage;
import com.lintang.netflik.movieQueryService.command.action.MovieCommandAction;
import com.lintang.netflik.movieQueryService.entity.ActorEntity;
import com.lintang.netflik.movieQueryService.entity.CreatorEntity;
import com.lintang.netflik.movieQueryService.entity.MovieEntity;
import com.lintang.netflik.movieQueryService.entity.VideoEntity;

import com.lintang.netflik.movieQueryService.repository.MovieRepository;
import com.lintang.netflik.movieQueryService.repository.VideoRepository;
import com.lintang.netflik.movieQueryService.util.entityMapper.ActorEntityMapper;
import com.lintang.netflik.movieQueryService.util.entityMapper.CreatorEntityMapper;
import com.lintang.netflik.movieQueryService.util.entityMapper.MovieEntityMapper;
import com.lintang.netflik.movieQueryService.util.entityMapper.VideoEntityMapper;
import com.lintang.netflik.movieQueryService.util.eventMapper.MovieEventMapper;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MovieCommandService {

    private MovieCommandAction movieCommandAction;

    @Autowired
    public MovieCommandService(MovieCommandAction movieCommandAction) {
        this.movieCommandAction = movieCommandAction;
    }

    public MovieEntity addMovie( AddMovieMessage newMovie) {
        MovieEntity newMovieEntity = movieCommandAction.addMovie(newMovie);
        return newMovieEntity;
    }


    public MovieEntity updateMovie(int movieId , @NotNull @Valid AddMovieMessage newMovie) {

        MovieEntity movie = movieCommandAction.updateMovie(movieId, newMovie);
        return movie;
    }





    public String deleteMovie(@NotNull @Valid int movieId) {
        String deleteRes = movieCommandAction.deleteMovie(movieId);
        return deleteRes;
    }

}
