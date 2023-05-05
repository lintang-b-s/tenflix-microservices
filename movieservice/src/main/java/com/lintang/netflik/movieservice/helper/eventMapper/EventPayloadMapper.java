package com.lintang.netflik.movieservice.helper.eventMapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lintang.netflik.movieservice.exception.MovieDomainException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class EventPayloadMapper {
    private final ObjectMapper objectMapper;
    public <T> T getMovieEventPayload (String payload , Class<T> outputType) {
        try {
            return objectMapper.readValue(payload, outputType);
        } catch (JsonProcessingException e) {
            log.error("Could not read {} object!", outputType.getName(), e);
            throw new MovieDomainException("could not read payload");
        }
    }


}
