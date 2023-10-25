package com.kafkastreams.movieservice.broker.publisher;


import com.kafkastreams.movieservice.broker.message.UploadVideoMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class MediaPublisher {

    @Autowired
    private KafkaTemplate<String, UploadVideoMessage> kafkaTemplate;

    public void publishToMediaService(UploadVideoMessage message) {
        kafkaTemplate.send("t.upload.request", message);
    }
}
