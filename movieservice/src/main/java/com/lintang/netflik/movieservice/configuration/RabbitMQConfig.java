package com.lintang.netflik.movieservice.configuration;


import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.messaging.support.MessageBuilder;

@Configuration
public class RabbitMQConfig {


    @Value("${rabbitmq.queue.email.name}")
    private String emailQueue;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.binding.email.routing.key}")
    private String emailRoutingKey;

    // for movie query service
    @Value("${rabbitmq.queue.movie.add}")
    private String addMovieQueue;
    @Value("${rabbitmq.binding.movieAdd.routing.key}")
    private String addMovieRoutingKey;
    @Value("${rabbitmq.queue.movie.update}")
    private String updateMovieQueue;
    @Value("${rabbitmq.queue.movieUpdate.routing.key}")
    private String updateMovieRoutingKey;
    @Value("${rabbitmq.queue.movie.delete}")
    private String deleteMovieQueue;
    @Value("${rabbitmq.queue.movieDelete.routing.key}")
    private String deleteMovieRoutingKey;



    // spring bean for queue - order queue
    @Bean
    public Queue emailQueue(){
        return new Queue(emailQueue);
    }
    @Bean
    public Queue addMovieQueue() {
        return new Queue(addMovieQueue);
    }
    @Bean
    public Queue updateMovieQueue() {
        return new Queue(updateMovieQueue);
    }
    @Bean
    public Queue deleteMovieQueue() {
        return new Queue(deleteMovieQueue);
    }

    // spring bean for exchange
    @Bean
    public TopicExchange exchange(){
        return new TopicExchange(exchange);
    }


    // spring bean for binding between exchange and queue using routing key
    @Bean
    public Binding emailBinding(){
        return BindingBuilder
                .bind(emailQueue())
                .to(exchange())
                .with(emailRoutingKey);
    }

    @Bean
    public Binding addMovieBinding(){
        return BindingBuilder
                .bind(addMovieQueue())
                .to(exchange())
                .with(addMovieRoutingKey);
    }

    @Bean
    public Binding updateMovieBinding(){
        return BindingBuilder
                .bind(updateMovieQueue())
                .to(exchange())
                .with(updateMovieRoutingKey);
    }
    @Bean
    public Binding deleteMovieBinding() {
        return BindingBuilder
                .bind(deleteMovieQueue())
                .to(exchange())
                .with(deleteMovieRoutingKey);
    }

    // message converter
//    @Bean
//    public MessageConverter converter(){
//        return new Jackson2JsonMessageConverter();
//    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        // Date and Time APIに対応したObjectMapperを作成する
        ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json()
                .modules(new JavaTimeModule())
                .dateFormat(new StdDateFormat())
                .timeZone("Asia/Tokyo")
                .build();
        Jackson2JsonMessageConverter jackson2JsonMessageConverter
                = new Jackson2JsonMessageConverter(objectMapper);
        return jackson2JsonMessageConverter;
    }

    // configure RabbitTemplate
    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter());
        return rabbitTemplate;
    }
}