package com.kafkastreams.movieservice.broker.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

//"t.upload.response"


import com.kafkastreams.movieservice.broker.message.UploadedVideoMessage;
import com.kafkastreams.movieservice.command.service.VideoCommandService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class UploadResponseListener {
    private static final Logger LOG = LoggerFactory.getLogger(UploadResponseListener.class);

    @Autowired
    private VideoCommandService videoCommandService;

     @Autowired
    private ObjectMapper objectMapper;


    @KafkaListener(topics = "t.upload.response", groupId = "lintangbs-videourl") //containerfactory  properties = {"spring.json.value.default.type=com.kafkastreams.movieservice.broker.message.UploadedVideoMessage"} gabisa
    public void listenPayBonus(@Payload String message,@Header(KafkaHeaders.OFFSET) String offset)  throws JsonMappingException, JsonProcessingException {
        
        LOG.info(" received uploaded video message from media service !! ");
        UploadedVideoMessage uploadedMessage =objectMapper.readValue(message, UploadedVideoMessage.class);
        videoCommandService.updateVideoUrl(uploadedMessage.getUrl(), uploadedMessage.getId());
        return;

    }

}
