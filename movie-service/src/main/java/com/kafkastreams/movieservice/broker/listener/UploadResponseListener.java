package com.kafkastreams.movieservice.broker.listener;

//"t.upload.response"


import com.kafkastreams.movieservice.broker.message.UploadedVideoMessage;
import com.kafkastreams.movieservice.command.service.VideoCommandService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class UploadResponseListener {
    private static final Logger LOG = LoggerFactory.getLogger(UploadResponseListener.class);

    @Autowired
    private VideoCommandService videoCommandService;

    @KafkaListener(topics = "t.upload.response")
    public void listenPayBonus(UploadedVideoMessage message) throws InterruptedException {
        LOG.info(" received uploaded video message from media service !! ");

        videoCommandService.updateVideoUrl(message.getUrl(), message.getId());
        return;

    }

}
