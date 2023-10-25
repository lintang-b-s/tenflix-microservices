package com.kafkastreams.movieservice.command.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kafkastreams.movieservice.command.action.*;
import com.kafkastreams.movieservice.api.request.AddMovieReq;
import com.kafkastreams.movieservice.broker.message.AddMovieMessage;
import com.kafkastreams.movieservice.broker.message.DeleteMovieMessage;
import com.kafkastreams.movieservice.broker.publisher.NotificationProducer;
import com.kafkastreams.movieservice.entity.MovieEntity;
import com.kafkastreams.movieservice.entity.OutboxEntity;
import com.kafkastreams.movieservice.entity.OutboxEventType;
import com.kafkastreams.movieservice.exception.InternalServerEx;
import com.kafkastreams.movieservice.util.messageMapper.MovieMessageMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;

@Service
public class MovieCommandService {
    private static final Logger LOG = LoggerFactory.getLogger(MovieCommandService.class);

    private MovieCommandAction movieCommandAction;

    private MovieMessageMapper messageMapper;
    private MovieOutboxAction outboxAction;
    private NotificationProducer notificationProducer;

    @Autowired
    public MovieCommandService(MovieCommandAction movieCommandAction, MovieMessageMapper messageMapper,
            MovieOutboxAction outboxAction, NotificationProducer notificationProducer) {
        this.movieCommandAction = movieCommandAction;
        this.messageMapper = messageMapper;
        this.outboxAction = outboxAction;
        this.notificationProducer = notificationProducer;
    }

    @Transactional
    public MovieEntity addMovie(@Valid AddMovieReq newMovie) {
        MovieEntity savedMovie = movieCommandAction.addMovie(newMovie);
        AddMovieMessage message = messageMapper.movieEntityToMessage(savedMovie);
        OutboxEntity movieOutbox = null;
        if (newMovie.getNotification()) {
            notificationProducer.sendMessageEmail(message);
            LOG.info("sending add movie event with id " + String.valueOf(savedMovie.getId())
                    + " to notification-service!");

        }
        LOG.info("sending add movie event with id " + String.valueOf(savedMovie.getId()) + " to movie-query-service!");

        try {
            movieOutbox = outboxAction.insertOutbox(
                    "movie.request",
                    String.valueOf(savedMovie.getId()),
                    OutboxEventType.ADD_MOVIE, message);

        } catch (JsonProcessingException e) {
            throw new InternalServerEx("error json processing : " + e.getMessage());
        }
        outboxAction.deleteOutbox(movieOutbox);
        return savedMovie;
    }

    @Transactional
    public MovieEntity updateMovie(int movieId, @Valid AddMovieReq newMovie) {
        MovieEntity updatedMovie = movieCommandAction.updateMovie(movieId, newMovie);
        AddMovieMessage message = messageMapper.movieEntityToMessage(updatedMovie);
        OutboxEntity movieOutbox = null;
        try {
            movieOutbox = outboxAction.insertOutbox(
                    "movie.request",
                    String.valueOf(updatedMovie.getId()),
                    OutboxEventType.UPDATE_MOVIE, message);
        } catch (JsonProcessingException e) {
            throw new InternalServerEx("error json processing : " + e.getMessage());
        }
        outboxAction.deleteOutbox(movieOutbox);
        LOG.info("sending update_movie message with id " + String.valueOf(updatedMovie.getId())
                + " to movie-query-service!");

        return updatedMovie;
    }

    @Transactional
    public String deleteMovie(int movieId) {
        String res = movieCommandAction.deleteMovie(movieId);
        DeleteMovieMessage deleteMovieMessage = DeleteMovieMessage.builder()
                .id(movieId)
                .build();
        OutboxEntity movieOutbox = null;
        try {
            movieOutbox = outboxAction.insertOutbox(
                    "movie.request",
                    String.valueOf(deleteMovieMessage.getId()),
                    OutboxEventType.DELETE_MOVIE, deleteMovieMessage);
        } catch (JsonProcessingException e) {
            throw new InternalServerEx("error json processing : " + e.getMessage());
        }
        outboxAction.deleteOutbox(movieOutbox);
        LOG.info("sending delete_movie message with id " + String.valueOf(deleteMovieMessage.getId())
                + " to movie-query-service!");

        return res;
    }

}
