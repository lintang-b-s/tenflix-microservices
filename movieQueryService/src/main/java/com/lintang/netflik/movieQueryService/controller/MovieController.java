package com.lintang.netflik.movieQueryService.controller;

import com.lintang.netflik.movieQueryService.dto.Movie;
//import com.lintang.netflik.movieQueryService.entity.GetAllMovies;
import com.lintang.netflik.movieQueryService.helper.DtoMapper.MovieDtoMapper;
import com.lintang.netflik.movieQueryService.service.MovieService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/query/v1/movies")
@AllArgsConstructor
public class MovieController {
    private MovieDtoMapper mapper;
    private MovieService movieService;




    @GetMapping
    public ResponseEntity<List<Movie>> getAllMovies() {
        return ok(mapper.toListModel(movieService.getMoviesByUserId(123)));

    }



    @GetMapping("/{movieId}")
    public ResponseEntity<Movie>  getMovieById(@PathVariable("movieId") int movieId) {
        return ok(mapper.movieEntitytoMovieDto(movieService.getMovieById(movieId)));
    }




}
