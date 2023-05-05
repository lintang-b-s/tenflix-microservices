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

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Component
public class MovieOutboxDataAccessMapper {

    private final ObjectMapper objectMapper;
    public MovieOutboxEntity movieOutboxMessageToOutboxEntity(MovieOutboxMessage
                                                              movieOutboxMessage) {
        return MovieOutboxEntity.builder()
                .id(movieOutboxMessage.getId())
                .createdAt(movieOutboxMessage.getCreatedAt())
                .processedAt(movieOutboxMessage.getProcessedAt())
                .type(movieOutboxMessage.getType())
                .payload(movieOutboxMessage.getPayload())
                .outboxStatus(movieOutboxMessage.getOutboxStatus())
                .version(movieOutboxMessage.getVersion())
                .build();
    }

    public MovieOutboxMessage movieOutboxEntityToMovieOutboxMessage(MovieOutboxEntity
                                                                    movieOutboxEntity) {
        return MovieOutboxMessage.builder()
                .id(movieOutboxEntity.getId())
                .createdAt(movieOutboxEntity.getCreatedAt())
                .processedAt(movieOutboxEntity.getProcessedAt())
                .type(movieOutboxEntity.getType())
                .payload(movieOutboxEntity.getPayload())
                .outboxStatus(movieOutboxEntity.getOutboxStatus())
                .version(movieOutboxEntity.getVersion())
                .build();
    }

    public List<MovieOutboxMessage> toListMovieOutboxMessage(List<MovieOutboxEntity> movieOutboxEntities) {
        return movieOutboxEntities.stream().map(e ->  movieOutboxEntityToMovieOutboxMessage(e)).collect(Collectors.toList());
    }

    public MovieOutboxEntity createMovieOutboxMessageEntity(AddMovieEvent addMovieEvent) {
        return MovieOutboxEntity.builder()
                .id(addMovieEvent.getId())
                .createdAt(ZonedDateTime.now())
                .type("create_movie")
                .payload(createPayload(addMovieEvent))
                .outboxStatus(OutboxStatus.STARTED)
                .build();
    }

    public MovieOutboxEntity notificationMovieOutboxMessageToEntity(AddMovieEvent addMovieEvent) {
        return MovieOutboxEntity.builder()
                .id(addMovieEvent.getId())
                .createdAt(ZonedDateTime.now())
                .type("notification_movie")
                .payload(createPayload(addMovieEvent))
                .outboxStatus(OutboxStatus.STARTED)
                .build();
    }

    public MovieOutboxEntity updateMovieOutboxMessageEntity(AddMovieEvent addMovieEvent) {
        return MovieOutboxEntity.builder()
                .id(addMovieEvent.getId())
                .createdAt(ZonedDateTime.now())
                .type("update_movie")
                .payload(createPayload(addMovieEvent))
                .outboxStatus(OutboxStatus.STARTED)
                .build();
    }

    public MovieOutboxEntity deleteMovieOutboxMessageEntity(AddMovieEvent addMovieEvent) {
        return MovieOutboxEntity.builder()
                .id(addMovieEvent.getId())
                .createdAt(ZonedDateTime.now())
                .type("delete_movie")
                .payload(createPayload(addMovieEvent))
                .outboxStatus(OutboxStatus.STARTED)
                .build();
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
