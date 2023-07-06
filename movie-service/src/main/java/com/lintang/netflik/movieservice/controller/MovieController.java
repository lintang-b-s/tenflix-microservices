package com.lintang.netflik.movieservice.controller;

import com.lintang.netflik.movieservice.dto.AddMovieReq;
import com.lintang.netflik.movieservice.dto.Movie;
import com.lintang.netflik.movieservice.helper.DtoMapper.MovieDtoMapper;
import com.lintang.netflik.movieservice.service.MovieService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/v1/movie-service/movies")
@AllArgsConstructor
@PreAuthorize("hasAuthority('ROLE_admin')")
public class MovieController {
    private MovieDtoMapper mapper;
    private MovieService movieService;



    @PostMapping
    public ResponseEntity<Movie> addMoviesWithVideo(@RequestBody AddMovieReq newMovie) {

        return ok(mapper.movieEntitytoMovieDto(movieService.addMovie(newMovie)));
    }



    @PutMapping("/{movieId}")
    public ResponseEntity<Movie> updateMovieById(@PathVariable(value = "movieId") int movieId, @RequestBody AddMovieReq updateMovie) {
        return ok(mapper.movieEntitytoMovieDto(movieService.updateMovie(movieId, updateMovie)));
    }



    @DeleteMapping("/{movieId}")
    public ResponseEntity<String> deleteMovieById(@PathVariable("movieId") int movieId) {
        return ok(movieService.deleteMovie(movieId));
    }



}
