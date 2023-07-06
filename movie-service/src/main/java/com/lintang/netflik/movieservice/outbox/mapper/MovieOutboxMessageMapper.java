package com.lintang.netflik.movieservice.outbox.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lintang.netflik.movieservice.event.AddMovieEvent;
import com.lintang.netflik.movieservice.event.AddMovieEventPayload;
import com.lintang.netflik.movieservice.outbox.entity.MovieOutboxEntity;
import com.lintang.netflik.movieservice.outbox.model.OutboxStatus;
import com.lintang.netflik.movieservice.outbox.model.movieQuery.MovieOutboxMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@AllArgsConstructor
public class MovieOutboxMessageMapper {
    private final ObjectMapper objectMapper;

    public MovieOutboxEntity movieOutboxMessageToMovieOutboxEntity(MovieOutboxMessage movieOutboxMessage) {

        MovieOutboxEntity movieOutboxEntity = new MovieOutboxEntity();
        log.info("idtes");
        log.info("id: {}", movieOutboxMessage.getId());
        movieOutboxEntity.setId(movieOutboxMessage.getId())
                .setCreatedAt(movieOutboxMessage.getCreatedAt())
                .setType(movieOutboxMessage.getType())
                .setPayload(movieOutboxMessage.getPayload())
                .setOutboxStatus(movieOutboxMessage.getOutboxStatus())
                .setVersion(movieOutboxMessage.getVersion());
        return movieOutboxEntity;
    }

    public MovieOutboxMessage updateMovieEventToOutboxMessage(AddMovieEvent movieEvent) {

        MovieOutboxMessage movieOutboxMessage = new MovieOutboxMessage();
        movieOutboxMessage.setId(UUID.randomUUID())
                .setCreatedAt(movieEvent.getCreatedAt())
                .setType("update_movie")
                .setPayload(createPayload(movieEvent))
                .setOutboxStatus(OutboxStatus.STARTED);
        return movieOutboxMessage;
    }

    public MovieOutboxMessage createMovieEventToOutboxMessage(AddMovieEvent movieEvent) {

        MovieOutboxMessage movieOutboxMessage = new MovieOutboxMessage();
        movieOutboxMessage.setId(UUID.randomUUID())
                .setCreatedAt(movieEvent.getCreatedAt())
                .setType("create_movie")
                .setPayload(createPayload(movieEvent))
                .setOutboxStatus(OutboxStatus.STARTED)
               ;
        return movieOutboxMessage;
    }

    public MovieOutboxMessage notificationMovieEventToOutboxMessage(AddMovieEvent movieEvent) {

        MovieOutboxMessage movieOutboxMessage = new MovieOutboxMessage();
        movieOutboxMessage.setId(UUID.randomUUID())
                .setCreatedAt(movieEvent.getCreatedAt())
                .setType("notification_movie")
                .setPayload(createPayload(movieEvent))
                .setOutboxStatus(OutboxStatus.STARTED)

                ;
        return movieOutboxMessage;
    }

    public MovieOutboxMessage deleteMovieEvenToOutboxMessage(AddMovieEvent movieEvent) {

        MovieOutboxMessage movieOutboxMessage = new MovieOutboxMessage();
        movieOutboxMessage.setId(UUID.randomUUID())
                .setCreatedAt(movieEvent.getCreatedAt())
                .setType("delete_movie")
                .setPayload(createPayload(movieEvent))
                .setOutboxStatus(OutboxStatus.STARTED)
                ;
        return movieOutboxMessage;
    }

    public String createPayload(AddMovieEvent addMovieEvent) {
        try {
            AddMovieEventPayload addMovieEventPayload =
                    AddMovieEventPayload.builder()
                            .id(addMovieEvent.getId())
                            .name(addMovieEvent.getName())
                            .type(addMovieEvent.getType())
                            .synopsis(addMovieEvent.getSynopsis())
                            .mpaRating(addMovieEvent.getMpaRating())
                            .rYear(addMovieEvent.getrYear())
                            .idmbRating(addMovieEvent.getIdmbRating())
                            .actors(addMovieEvent.getActors())
                            .creators(addMovieEvent.getCreators())
                            .videos(addMovieEvent.getVideos())
                            .outboxType("create_movie")
                            .image(addMovieEvent.getImage())
                            .build();

            return objectMapper.writeValueAsString(addMovieEventPayload);
        } catch (JsonProcessingException e) {
            String error = "Could not create addMovieEventPayload object for  id: {}" + addMovieEvent.getId()
                    + e;
            log.error(error);
            return error;
        }


    }
}
