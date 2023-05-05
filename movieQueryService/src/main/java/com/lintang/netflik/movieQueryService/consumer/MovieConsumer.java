package com.lintang.netflik.movieQueryService.consumer;


import com.lintang.netflik.movieQueryService.event.AddMovieEvent;
import com.lintang.netflik.movieQueryService.service.MovieService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class MovieConsumer {
    private MovieService movieService;

    @RabbitListener(queues = "add_movie")
    public void consumeAddMovie(AddMovieEvent event) {
        log.info("addMovieEvent recceived with id: {}", event.getId());
        movieService.addMovie(event);
        log.info("movie created!");
    }

    @RabbitListener(queues = "update_movie")
    public  void consumeUpdateMovie(AddMovieEvent event) {
        log.info("updateMovie recceived with id: {}", event.getId());
        movieService.updateMovie(event);
        log.info("movie updated!");
    }

    @RabbitListener(queues = "delete_movie")
    public void consumeDeleteMovie(AddMovieEvent event) {
        log.info("deleteMovieEvent recceived with id: {}", event.getId());
        movieService.deleteMovie(event.getId());
        log.info("movie deleted!");
    }
}
