package com.kafkastreams.movieservice.broker.publisher;

import com.kafkastreams.movieservice.broker.message.AddMovieMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class NotificationProducer {
    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.binding.email.routing.key}")
    private String emailRoutingKey;


    private RabbitTemplate rabbitTemplate;

    public NotificationProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessageEmail(AddMovieMessage addMovieMessage) {
        rabbitTemplate.convertAndSend(exchange, emailRoutingKey, addMovieMessage);
    }
}

