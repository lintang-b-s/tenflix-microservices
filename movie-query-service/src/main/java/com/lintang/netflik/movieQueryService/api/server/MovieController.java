package com.lintang.netflik.movieQueryService.api.server;

import com.lintang.netflik.movieQueryService.broker.message.Movie;
import com.lintang.netflik.movieQueryService.command.service.MovieCommandService;

import com.lintang.netflik.movieQueryService.query.service.MovieQueryService;
import com.lintang.netflik.movieQueryService.util.DtoMapper.MovieDtoMapper;
import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/v1/movie-query/movies")
@PreAuthorize("hasAuthority('ROLE_user')")
public class MovieController {
    private MovieDtoMapper mapper;
    private MovieCommandService movieService;

    private MovieQueryService movieQueryService;

    @Autowired
    public MovieController(MovieDtoMapper mapper, MovieCommandService movieCommandService, MovieQueryService movieQueryService) {
        this.mapper = mapper;
        this.movieService = movieCommandService;
        this.movieQueryService = movieQueryService;
    }


    // @Summary     Get all movies
// @Description  Get all movies
// @ID          get-all-movies
// @Tags  	    movie
// @Accept      json
// @Produce     json
// @Success     200 {object} ResponseEntity<List<Movie>>
// @Failure     400 {object} response
// @Failure     500 {object} response
// @Router      /api/v1/movie-query/movies/ [get]
// Author: https://github.com/lintang-b-s
    @GetMapping
    public ResponseEntity<List<Movie>>  getAllMoveis(@AuthenticationPrincipal Jwt principal) {
        return ok(mapper.toListModel(movieQueryService.getAllMovie(principal.getSubject())));
    }


    // @Summary     Get movie by   movieId
// @Description  Get movie  by  movieId
// @ID          get-movie
// @Tags  	    movie
// @Accept      json
// @Produce     json
// @Param       request body movieId true "Set up movieId"
// @Success     200 {object} ResponseEntity<Movie>
// @Failure     400 {object} response
// @Failure     500 {object} response
// @Router      /api/v1/movie-query/movies/{movieId} [get]
// Author: https://github.com/lintang-b-s
    @GetMapping("/{movieId}")
    public ResponseEntity<Movie>  getMovieById(@PathVariable("movieId") int movieId, @AuthenticationPrincipal Jwt principal) {
        return ok(mapper.movieEntitytoMovieDto(movieQueryService.getMovieById(movieId, principal.getSubject())));
    }


}
