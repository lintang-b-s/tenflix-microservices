package com.lintang.netflik.movieservice.outbox.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lintang.netflik.movieservice.outbox.entity.MovieOutboxEntity;
import com.lintang.netflik.movieservice.outbox.model.movieQuery.MovieOutboxMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Component
public class MovieOutboxDataAccessMapper {

    private final ObjectMapper objectMapper;
    private final MovieOutboxMessageMapper movieOutboxMessageMapper;
    public MovieOutboxEntity movieOutboxMessageToOutboxEntity(MovieOutboxMessage
                                                              movieOutboxMessage) {

        MovieOutboxEntity entity= new MovieOutboxEntity();

                entity.setId(movieOutboxMessage.getId())
                .setCreatedAt(movieOutboxMessage.getCreatedAt())
                .setProcessedAt(movieOutboxMessage.getProcessedAt())
                .setType(movieOutboxMessage.getType())
                .setPayload(movieOutboxMessage.getPayload())
                .setOutboxStatus(movieOutboxMessage.getOutboxStatus())
                .setVersion(movieOutboxMessage.getVersion());

        return entity;
    }

    public MovieOutboxMessage movieOutboxEntityToMovieOutboxMessage(MovieOutboxEntity
                                                                    movieOutboxEntity) {

        MovieOutboxMessage movieOutboxMessage = new MovieOutboxMessage();
        movieOutboxMessage.setId(movieOutboxEntity.getId())
                .setCreatedAt(movieOutboxEntity.getCreatedAt())
                .setProcessedAt(movieOutboxEntity.getProcessedAt())
                .setType(movieOutboxEntity.getType())
                .setPayload(movieOutboxEntity.getPayload())
                .setOutboxStatus(movieOutboxEntity.getOutboxStatus())
                .setVersion(movieOutboxEntity.getVersion());
        return movieOutboxMessage;
    }

    public List<MovieOutboxMessage> toListMovieOutboxMessage(List<MovieOutboxEntity> movieOutboxEntities) {
        return movieOutboxEntities.stream().map(e ->  movieOutboxEntityToMovieOutboxMessage(e)).collect(Collectors.toList());
    }

    public MovieOutboxEntity createMovieOutboxMessageEntity(MovieOutboxMessage movieOutboxMessage) {
        return movieOutboxMessageMapper.movieOutboxMessageToMovieOutboxEntity(movieOutboxMessage);
    }

    public MovieOutboxEntity notificationMovieOutboxMessageToEntity(MovieOutboxMessage movieOutboxMessage) {
        return movieOutboxMessageMapper.movieOutboxMessageToMovieOutboxEntity(movieOutboxMessage);
    }

    public MovieOutboxEntity updateMovieOutboxMessageToEntity(MovieOutboxMessage movieOutboxMessage) {
        return movieOutboxMessageMapper.movieOutboxMessageToMovieOutboxEntity(movieOutboxMessage);
    }

    public MovieOutboxEntity deleteMovieOutboxMessageEntity(MovieOutboxMessage movieOutboxMessage) {
        return movieOutboxMessageMapper.movieOutboxMessageToMovieOutboxEntity(movieOutboxMessage);
    }



}
