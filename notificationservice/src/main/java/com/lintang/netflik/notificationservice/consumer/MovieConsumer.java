package com.lintang.netflik.notificationservice.consumer;

import com.lintang.netflik.notificationservice.dto.MovieEvent;
import com.lintang.netflik.notificationservice.dto.ResponseMessage;
import com.lintang.netflik.notificationservice.service.EmailConsumerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;
//import org.springframework.web.reactive.function.client.WebClient;
//import reactor.core.publisher.Mono;

@Slf4j
@Service

public class MovieConsumer {

//    private WebClient webClient;
    private  EmailConsumerService emailService;
    public MovieConsumer( EmailConsumerService emailService) {this.emailService =  emailService;}

    @RabbitListener(queues = "${rabbitmq.queue.email.name}")
    public void consume(MovieEvent event){
        log.info("sending email!");
        emailService.emailNotifNewMovie(event);
        log.info("email send!");

//        Message message = new Message();
//        message.setMessageContentBody(event.getPayload().getMessageContentBody())
//                        .setMovieTitle(event.getPayload().getMovieTitle())
//                                .setImageUrl(event.getPayload().getImageUrl()) ;
//        ResponseMessage res = new ResponseMessage();
//        webClient.post()
//                .uri("http:://localhost:8081/ws/message")
//                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//                        .body(Mono.just(message), Message.class)
//                                .retrieve()
//                                        .bodyToMono(ResponseMessage.class);
//
//        event.getPayload().setStatus("COMPLETED");
//        return;
    }
}
