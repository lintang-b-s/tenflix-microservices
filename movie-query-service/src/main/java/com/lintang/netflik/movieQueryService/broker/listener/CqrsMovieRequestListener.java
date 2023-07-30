package com.lintang.netflik.movieQueryService.broker.listener;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lintang.netflik.movieQueryService.broker.message.AddMovieMessage;
import com.lintang.netflik.movieQueryService.broker.message.DeleteMovieMessage;
import com.lintang.netflik.movieQueryService.broker.message.OutboxMessage;
import com.lintang.netflik.movieQueryService.command.service.MovieCommandService;
import com.lintang.netflik.movieQueryService.entity.OutboxEventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.apache.commons.lang3.StringUtils;


@Component
public class CqrsMovieRequestListener {
    private static final Logger LOG = LoggerFactory.getLogger(CqrsMovieRequestListener.class);
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MovieCommandService movieCommandService;

    @KafkaListener(topics = "t.cqrs.movie.request", containerFactory = "stringDeserializerContainerFactory")
    public void onMovieDataChanged(@Header(name = KafkaHeaders.RECEIVED_TIMESTAMP) long timestamp,
                                  @Payload String message) throws JsonMappingException, JsonProcessingException {
        var outboxMessage = objectMapper.readValue(message, OutboxMessage.class);
        LOG.info("get movie message from movie-service!!");
        if (StringUtils.equalsAny(outboxMessage.getPayload().getEventType(), OutboxEventType.ADD_MOVIE,
                OutboxEventType.UPDATE_MOVIE)) {

            var addMovieMessage = objectMapper.readValue(outboxMessage.getPayload().getPayload(),
                    AddMovieMessage.class);

            switch (outboxMessage.getPayload().getEventType()) {
                case OutboxEventType.ADD_MOVIE:
                    movieCommandService.addMovie(addMovieMessage);
                    LOG.info("add movie with id : " + addMovieMessage.getId() + " to movie-query database!" );
                    break;
                case OutboxEventType.UPDATE_MOVIE:
                    movieCommandService.updateMovie(addMovieMessage.getId(), addMovieMessage);
                    LOG.info("update movie with id : " + addMovieMessage.getId() + " in movie-query database!" );
                    break;

            }
        } else if(StringUtils.equalsAny(outboxMessage.getPayload().getEventType(), OutboxEventType.DELETE_MOVIE)) {
            var deleteMovieMessage = objectMapper.readValue(outboxMessage.getPayload().getPayload(),
                    DeleteMovieMessage.class);
            movieCommandService.deleteMovie(deleteMovieMessage.getId());
            LOG.info("delete movie with id : " + deleteMovieMessage.getId() + " in movie-query database!" );
        }

    }



}
