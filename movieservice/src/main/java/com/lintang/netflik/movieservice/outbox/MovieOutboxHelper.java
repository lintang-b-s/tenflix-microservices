package com.lintang.netflik.movieservice.outbox;

import com.lintang.netflik.movieservice.dto.MovieEvent;
import com.lintang.netflik.movieservice.entity.MovieEntity;
import com.lintang.netflik.movieservice.event.AddMovieEvent;
import com.lintang.netflik.movieservice.outbox.entity.MovieOutboxEntity;
import com.lintang.netflik.movieservice.outbox.mapper.MovieOutboxDataAccessMapper;
import com.lintang.netflik.movieservice.outbox.model.OutboxStatus;
import com.lintang.netflik.movieservice.outbox.model.movieQuery.MovieOutboxMessage;
import com.lintang.netflik.movieservice.outbox.repository.MovieOutboxRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@AllArgsConstructor
public class MovieOutboxHelper {

    private MovieOutboxRepository movieOutboxRepository;
    private MovieOutboxDataAccessMapper movieOutboxDataAccessMapper;

    public void createMovieOutboxMessage(AddMovieEvent movieEvent) {

        log.info("movie created with id: {}", movieEvent.getId());

        movieOutboxRepository.save(movieOutboxDataAccessMapper.
                        createMovieOutboxMessageEntity(movieEvent));
        return ;
    }

    public void notificationMovieOutboxMessage(AddMovieEvent movieEvent) {
        log.info("movie event saved with id: {}", movieEvent.getId());
        movieOutboxRepository.save(movieOutboxDataAccessMapper.
                notificationMovieOutboxMessageToEntity(movieEvent));
    }

    public void updateMovieOutboxMessage(AddMovieEvent movieEvent) {
        log.info("movie updated with id: {}", movieEvent.getId());

        movieOutboxRepository.save(movieOutboxDataAccessMapper.
                    updateMovieOutboxMessageEntity(movieEvent));
    }

    public void deleteMovieOutboxMessage(AddMovieEvent movieEvent) {
        log.info("movie deleted with id: {}", movieEvent.getId());

        movieOutboxRepository.save(movieOutboxDataAccessMapper.
                deleteMovieOutboxMessageEntity(movieEvent) );
    }

    public Optional<List<MovieOutboxMessage>> getMovieOutboxMessageByOutboxStatusAndType(OutboxStatus outboxStatus,
                                                                                         String type) {
        Optional<List<MovieOutboxMessage>> movieOutboxMessageList =
                Optional.of(
                movieOutboxDataAccessMapper.toListMovieOutboxMessage(
                movieOutboxRepository.findByTypeAndOutboxStatus(outboxStatus, type).get()));


        return movieOutboxMessageList;
    }

    public Optional<List<MovieOutboxMessage>> getMovieOutboxMessageByOutboxStatus(OutboxStatus outboxStatus) {
        Optional<List<MovieOutboxMessage>> movieOutboxMessageList =
                Optional.of(
                        movieOutboxDataAccessMapper.toListMovieOutboxMessage(
                                movieOutboxRepository.findByOutboxStatus(outboxStatus).get()));


        return movieOutboxMessageList;
    }

    @Transactional
    public void deleteMovieOutboxMessageByOutboxStatus(OutboxStatus outboxStatus) {
        movieOutboxRepository.deleteByOutboxStatus(outboxStatus);
    }
}
