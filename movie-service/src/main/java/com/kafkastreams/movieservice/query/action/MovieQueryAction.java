package com.kafkastreams.movieservice.query.action;

import com.kafkastreams.movieservice.entity.MovieEntity;
import com.kafkastreams.movieservice.exception.ResourceNotFoundException;
import com.kafkastreams.movieservice.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MovieQueryAction {
    private MovieRepository movieRepository;

    @Autowired
    public MovieQueryAction(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public MovieEntity getMovieById(int id){
        Optional<MovieEntity> movie = movieRepository.findById(id);
        if (movie.isEmpty()){
            throw new ResourceNotFoundException("movie with  id " + id + " not found");
        }


        return movie.get() ;
    }
}
