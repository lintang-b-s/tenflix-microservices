package com.lintang.netflik.movieQueryService.broker.listener;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lintang.netflik.movieQueryService.broker.message.AddVideoMessage;
import com.lintang.netflik.movieQueryService.broker.message.DeleteVideoMessage;
import com.lintang.netflik.movieQueryService.broker.message.OutboxMessage;
import com.lintang.netflik.movieQueryService.command.service.MovieCommandService;
import com.lintang.netflik.movieQueryService.command.service.VideoCommandService;
import com.lintang.netflik.movieQueryService.entity.OutboxEventType;
import com.lintang.netflik.movieQueryService.entity.VideoEntity;
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
public class CqrsVideoRequestListener {

    private static final Logger LOG = LoggerFactory.getLogger(CqrsVideoRequestListener.class);
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private VideoCommandService videoCommandService;

    @KafkaListener(topics = "t.cqrs.video.request", containerFactory = "stringDeserializerContainerFactory")
    public void onVideoDataChanged(@Header(name = KafkaHeaders.RECEIVED_TIMESTAMP) long timestamp,
                                   @Payload String message) throws JsonMappingException, JsonProcessingException {
        var outboxMessage = objectMapper.readValue(message, OutboxMessage.class);
        LOG.info("get video message from movie-service!");
        if (StringUtils.equalsAny(outboxMessage.getPayload().getEventType(),
                OutboxEventType.ADD_VIDEO_TO_MOVIE, OutboxEventType.UPDATE_VIDEO_FROM_MOVIE,
                OutboxEventType.ADD_VIDEO_AND_UPLOAD)) {
            var addVideoMessage = objectMapper.readValue(outboxMessage.getPayload().getPayload(),
                    AddVideoMessage.class);

            switch (outboxMessage.getPayload().getEventType()) {
                case OutboxEventType.ADD_VIDEO_TO_MOVIE:
                    VideoEntity video =videoCommandService.addVideoToMovie(addVideoMessage);
                    LOG.info("add video with id "+ addVideoMessage.getId() + " to movie " + addVideoMessage.getMovieId());
                    break;
                case OutboxEventType.UPDATE_VIDEO_FROM_MOVIE:
                    videoCommandService.updateVideoFromMovie(addVideoMessage);
                    LOG.info("update video with id "+ addVideoMessage.getId() + " to movie " + addVideoMessage.getMovieId());
                    break;
                case OutboxEventType.ADD_VIDEO_AND_UPLOAD:
                    videoCommandService.addVideoAndUpload(addVideoMessage);
                    LOG.info("add video without url with id "+ addVideoMessage.getId() + " to movie " + addVideoMessage.getMovieId());
                    break;
                case OutboxEventType.UPDATE_VIDEO_URL:
                    videoCommandService.updateVideoUrl(addVideoMessage);
                    LOG.info("update video url  with id "+ addVideoMessage.getId() + " to movie " + addVideoMessage.getMovieId());
                    break;
            }
        } else if(StringUtils.equalsAny(outboxMessage.getPayload().getEventType(),
                OutboxEventType.DELETE_VIDEO_FROM_MOVIE)) {
            var deleteVideoMessage = objectMapper.readValue(outboxMessage.getPayload().getPayload(),
                    DeleteVideoMessage.class);

            videoCommandService.deleteVideoFromMovie(deleteVideoMessage);

            LOG.info("delete video with id "+ deleteVideoMessage.getId() );

        }
    }
}
