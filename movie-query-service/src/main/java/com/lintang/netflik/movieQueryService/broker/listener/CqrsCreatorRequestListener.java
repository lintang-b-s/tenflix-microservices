package com.lintang.netflik.movieQueryService.broker.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lintang.netflik.movieQueryService.broker.message.OutboxMessage;
import com.lintang.netflik.movieQueryService.broker.message.UpdateCreatorMessage;
import com.lintang.netflik.movieQueryService.command.service.CreatorCommandService;
import com.lintang.netflik.movieQueryService.entity.OutboxEventType;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class CqrsCreatorRequestListener {

    private static final Logger LOG = LoggerFactory.getLogger(CqrsCreatorRequestListener.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CreatorCommandService creatorCommandService;

    @KafkaListener(topics = "t.cqrs.creator.request", containerFactory = "stringDeserializerContainerFactory")
    public void onMovieDataChanged(@Header(name = KafkaHeaders.RECEIVED_TIMESTAMP) long timestamp,
                                   @Payload String message) throws JsonMappingException, JsonProcessingException {
        var outboxMessage = objectMapper.readValue(message, OutboxMessage.class);
        LOG.info("get creator message from movie-service!!");
        if (StringUtils.equalsAny(outboxMessage.getPayload().getEventType(),
                OutboxEventType.UPDATE_CREATOR)) {
            var updateCreatorMessage = objectMapper.readValue(outboxMessage.getPayload().getPayload(),
                    UpdateCreatorMessage.class);
            creatorCommandService.updateCreator(updateCreatorMessage);
        }
    }
}
