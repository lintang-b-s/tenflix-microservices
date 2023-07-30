package com.lintang.netflik.movieservice.api.server;

import com.lintang.netflik.movieservice.api.request.AddMovieReq;
import com.lintang.netflik.movieservice.api.response.Movie;
import com.lintang.netflik.movieservice.command.service.MovieCommandService;
import com.lintang.netflik.movieservice.util.DtoMapper.MovieDtoMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/v1/movie-service/movies")
@AllArgsConstructor
@PreAuthorize("hasAuthority('ROLE_default-roles-tenflix')")
public class MovieController {
    private MovieDtoMapper mapper;
    private MovieCommandService movieService;



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
