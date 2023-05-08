package com.lintang.netflik.movieservice.publisher;



import com.lintang.netflik.movieservice.dto.MovieEvent;
import com.lintang.netflik.movieservice.event.AddMovieEvent;
import com.lintang.netflik.movieservice.event.AddMovieEventPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MovieProducer {
    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.binding.email.routing.key}")
    private String emailRoutingKey;
    @Value("${rabbitmq.binding.movieAdd.routing.key}")
    private String addMovieRoutingKey;
    @Value("${rabbitmq.queue.movieUpdate.routing.key}")
    private String updateMovieRoutingKey;
    @Value("${rabbitmq.queue.movieDelete.routing.key}")
    private String deleteMovieRoutingKey;


    private RabbitTemplate rabbitTemplate;

    public MovieProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessageEmail(AddMovieEvent addMovieEvent) {
        rabbitTemplate.convertAndSend(exchange, emailRoutingKey, addMovieEvent);
    }

    public void sendMessageAddMovie( AddMovieEvent addMovieEvent) {
        rabbitTemplate.convertAndSend(exchange,addMovieRoutingKey, addMovieEvent );
    }

    public void sendMessageUpdateMovie(AddMovieEvent addMovieEvent) {
        rabbitTemplate.convertAndSend(exchange, updateMovieRoutingKey, addMovieEvent);
    }

    public void sendMessageDeleteMovie(AddMovieEvent addMovieEvent) {
        rabbitTemplate.convertAndSend(exchange, deleteMovieRoutingKey, addMovieEvent);
    }
}
