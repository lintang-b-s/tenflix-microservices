package com.lintang.netflik.movieQueryService.broker.listener;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lintang.netflik.movieQueryService.broker.message.AddMovieMessage;
import com.lintang.netflik.movieQueryService.broker.message.OutboxMessage;
import com.lintang.netflik.movieQueryService.broker.message.UpdateCategoryMessage;
import com.lintang.netflik.movieQueryService.command.service.CategoryCommandService;
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
public class CqrsCategoryRequestListener {
    private static final Logger LOG = LoggerFactory.getLogger(CqrsCategoryRequestListener.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CategoryCommandService categoryCommandService;

    @KafkaListener(topics = "t.cqrs.category.request", containerFactory = "stringDeserializerContainerFactory")
    public void onMovieDataChanged(@Header(name = KafkaHeaders.RECEIVED_TIMESTAMP) long timestamp,
                                   @Payload String message) throws JsonMappingException, JsonProcessingException {
        var outboxMessage = objectMapper.readValue(message, OutboxMessage.class);
        LOG.info("get category message from movie-service!!");
        if (StringUtils.equalsAny(outboxMessage.getPayload().getEventType(),
                OutboxEventType.UPDATE_CATEGORY)) {
            var updateCategoryMessage =  objectMapper.readValue(outboxMessage.getPayload().getPayload(),
                    UpdateCategoryMessage.class);

            categoryCommandService.updateCategory(updateCategoryMessage);

        }


    }

}
