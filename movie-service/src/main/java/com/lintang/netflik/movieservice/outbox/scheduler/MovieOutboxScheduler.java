package com.lintang.netflik.movieservice.outbox.scheduler;


import com.lintang.netflik.movieservice.event.AddMovieEventPayload;
import com.lintang.netflik.movieservice.helper.eventMapper.EventPayloadMapper;
import com.lintang.netflik.movieservice.helper.eventMapper.MovieEventMapper;
import com.lintang.netflik.movieservice.outbox.MovieOutboxHelper;
import com.lintang.netflik.movieservice.outbox.mapper.MovieOutboxDataAccessMapper;
import com.lintang.netflik.movieservice.outbox.model.OutboxStatus;
import com.lintang.netflik.movieservice.outbox.model.movieQuery.MovieOutboxMessage;
import com.lintang.netflik.movieservice.outbox.repository.MovieOutboxRepository;
import com.lintang.netflik.movieservice.publisher.MovieProducer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
@Component
@AllArgsConstructor
public class MovieOutboxScheduler  {
    private final MovieOutboxHelper movieOutboxHelper;
    private final MovieProducer producer;
    private final MovieOutboxRepository movieOutboxRepository;
    private final MovieOutboxDataAccessMapper mapper;
    private final EventPayloadMapper eventPayloadMapper;
    private MovieEventMapper movieEventMapper;


    @Transactional
    @Scheduled(fixedDelay = 1000,
            initialDelay = 1000)
    public void processCreateMovieOutboxMessage() {
        Optional<List<MovieOutboxMessage>> outboxMessageResponses=
                movieOutboxHelper.getMovieOutboxMessageByOutboxStatusAndType(
                        OutboxStatus.STARTED,
                        "create_movie"
                );


        if (outboxMessageResponses.isPresent() && outboxMessageResponses.get().size() > 0) {
            List<MovieOutboxMessage> outboxMessages = outboxMessageResponses.get();

            log.info("version movieOutboxMessage in get scheduler: {}",  outboxMessages.stream().map(movieOutboxMessage ->

                String.valueOf(movieOutboxMessage.getVersion())

            ).collect(Collectors.joining(",")));


            log.info("Received {} movieOutboxCreateMessage with ids: {} sending to message bus!",
                    outboxMessages.size(),
                    outboxMessages.stream().map(outboxMessage ->
                        String.valueOf(outboxMessage.getId())
                    ).collect(Collectors.joining(",")));

            outboxMessages.forEach(outboxMessage -> {
                producer.sendMessageAddMovie(movieEventMapper.addMovieEventPayloadToAddMovieEvent(eventPayloadMapper.getMovieEventPayload(outboxMessage.getPayload(),
                        AddMovieEventPayload.class)));
                updateOutboxStatus(outboxMessage, OutboxStatus.COMPLETED);
            });
            log.info("{} CreateMovieOutboxMessage sent to message bus!", outboxMessages.size());
        }
    }

    @Transactional
    @Scheduled(fixedDelay = 1000,
            initialDelay = 1000)
    public void processNotificationMovieOutboxMessage() {
        Optional<List<MovieOutboxMessage>> outboxMessageResponses =
                movieOutboxHelper.getMovieOutboxMessageByOutboxStatusAndType(
                        OutboxStatus.STARTED,
                        "notification_movie"
                );
        if (outboxMessageResponses.isPresent() &&  outboxMessageResponses.get().size() >0 ) {
            List<MovieOutboxMessage> outboxMessages = outboxMessageResponses.get();
            log.info("Received {} MovieOutboxNotificationMessage with ids: {} sending to message bus",

                    outboxMessages.size(),
                    outboxMessages.stream().map(outboxMessage ->
                            String.valueOf(outboxMessage.getId())
                    ).collect(Collectors.joining(",")));
            outboxMessages.forEach(outboxMessage -> {
                producer.sendMessageEmail( movieEventMapper.addMovieEventPayloadToAddMovieEvent( eventPayloadMapper.getMovieEventPayload(outboxMessage.getPayload(),
                        AddMovieEventPayload.class)));
                updateOutboxStatus(outboxMessage, OutboxStatus.COMPLETED);
            });
            log.info("{} NotificationMovieOutboxMessage sent to message bus!", outboxMessages.size());
        }
    }

    @Transactional
    @Scheduled(fixedDelay = 5000,
            initialDelay = 5000)
    public void processUpdateMovieOutboxMessage() {
        Optional<List<MovieOutboxMessage>> outboxMessageResponses=
                movieOutboxHelper.getMovieOutboxMessageByOutboxStatusAndType(
                        OutboxStatus.STARTED,
                        "update_movie"
                );

        if (outboxMessageResponses.isPresent() && outboxMessageResponses.get().size() > 0) {
            List<MovieOutboxMessage> outboxMessages = outboxMessageResponses.get();
            log.info("Received {} movieOutboxUpdateMessage with ids: {} sending to message bus!",
                    outboxMessages.size(),
                    outboxMessages.stream().map(outboxMessage ->
                            String.valueOf(outboxMessage.getId())
                    ).collect(Collectors.joining(",")));
            outboxMessages.forEach(outboxMessage -> {
                producer.sendMessageUpdateMovie( movieEventMapper.addMovieEventPayloadToAddMovieEvent( eventPayloadMapper.getMovieEventPayload(outboxMessage.getPayload(),
                        AddMovieEventPayload.class)));
                updateOutboxStatus(outboxMessage, OutboxStatus.COMPLETED);
            });
            log.info("{} UpdateMovieOutboxMessage sent to message bus!", outboxMessages.size());
        }

    }


    @Transactional
    @Scheduled(fixedDelay = 1000,
            initialDelay = 1000)
    public void processDeleteMovieOutboxMessage() {
        Optional<List<MovieOutboxMessage>> outboxMessageResponses=
                movieOutboxHelper.getMovieOutboxMessageByOutboxStatusAndType(
                        OutboxStatus.STARTED,
                        "delete_movie"
                );

        if (outboxMessageResponses.isPresent() && outboxMessageResponses.get().size() > 0) {
            List<MovieOutboxMessage> outboxMessages = outboxMessageResponses.get();
            log.info("Received {} movieOutboxDeleteMessage with ids: {} sending to message bus!",
                    outboxMessages.size(),
                    outboxMessages.stream().map(outboxMessage ->
                            String.valueOf(outboxMessage.getId())
                    ).collect(Collectors.joining(",")));
            outboxMessages.forEach(outboxMessage -> {
                producer.sendMessageDeleteMovie( movieEventMapper.addMovieEventPayloadToAddMovieEvent( eventPayloadMapper.getMovieEventPayload(outboxMessage.getPayload(),
                        AddMovieEventPayload.class)));
                updateOutboxStatus(outboxMessage, OutboxStatus.COMPLETED);
            });
            log.info("{} DeleteMovieOutboxMessage sent to message bus!", outboxMessages.size());
        }
    }






    private void updateOutboxStatus(MovieOutboxMessage movieOutboxMessage, OutboxStatus outboxStatus) {
        movieOutboxMessage.setOutboxStatus(outboxStatus);
        movieOutboxMessage.setProcessedAt(ZonedDateTime.now());
        movieOutboxRepository.save(mapper.movieOutboxMessageToOutboxEntity(movieOutboxMessage));
        log.info("MovieOutboxMessage is updated with outbox status: {}", outboxStatus.name());
    }
}
