package com.lintang.netflik.movieservice.publisher;



import com.lintang.netflik.movieservice.dto.MovieEvent;
import com.lintang.netflik.movieservice.event.AddMovieEvent;
import com.lintang.netflik.movieservice.outbox.model.movieQuery.MovieOutboxMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    public void sendMessageEmail(MovieOutboxMessage movieOutboxMessage) {
        rabbitTemplate.convertAndSend(exchange, emailRoutingKey, movieOutboxMessage);
    }

    public void sendMessageAddMovie(MovieOutboxMessage movieOutboxMessage) {
        rabbitTemplate.convertAndSend(exchange,addMovieRoutingKey, movieOutboxMessage );
    }

    public void sendMessageUpdateMovie(MovieOutboxMessage movieOutboxMessage) {
        rabbitTemplate.convertAndSend(exchange, updateMovieRoutingKey, movieOutboxMessage);
    }

    public void sendMessageDeleteMovie(MovieOutboxMessage movieOutboxMessage) {
        rabbitTemplate.convertAndSend(exchange, deleteMovieRoutingKey, movieOutboxMessage);
    }
}
