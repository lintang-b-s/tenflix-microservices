package com.lintang.netflik.movieservice.controller;
import com.lintang.netflik.movieservice.dto.*;
import com.lintang.netflik.movieservice.helper.DtoMapper.MovieDtoMapper;
import com.lintang.netflik.movieservice.publisher.MovieProducer;
import com.lintang.netflik.movieservice.service.MovieService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/v1/movies")
@AllArgsConstructor
public class MovieController {
    private MovieDtoMapper mapper;
    private MovieService movieService;



    @PostMapping
    public ResponseEntity<Movie> addMoviesWithVideo(@RequestBody AddMovieReq newMovie) {

        return ok(mapper.movieEntitytoMovieDto(movieService.addMovie(newMovie)));
    }

    @GetMapping
    public ResponseEntity<List<Movie>> getAllMovies() {
        return ok(mapper.toListModel(movieService.getMoviesByUserId(123)));

    }

    @PutMapping("/{movieId}")
    public ResponseEntity<Movie> updateMovieById(@PathVariable(value = "movieId") int movieId, @RequestBody AddMovieReq updateMovie) {
        return ok(mapper.movieEntitytoMovieDto(movieService.updateMovie(movieId, updateMovie)));
    }

    @GetMapping("/{movieId}")
    public ResponseEntity<Movie>  getMovieById(@PathVariable("movieId") int movieId) {
        return ok(mapper.movieEntitytoMovieDto(movieService.getMovieById(movieId)));
    }

    @DeleteMapping("/{movieId}")
    public ResponseEntity<String> deleteMovieById(@PathVariable("movieId") int movieId) {
        return ok(movieService.deleteMovie(movieId));
    }



}
