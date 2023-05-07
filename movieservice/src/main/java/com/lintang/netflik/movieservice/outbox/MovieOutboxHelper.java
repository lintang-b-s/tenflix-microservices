package com.lintang.netflik.movieservice.outbox;

import com.lintang.netflik.movieservice.dto.MovieEvent;
import com.lintang.netflik.movieservice.entity.MovieEntity;
import com.lintang.netflik.movieservice.event.AddMovieEvent;
import com.lintang.netflik.movieservice.outbox.entity.MovieOutboxEntity;
import com.lintang.netflik.movieservice.outbox.mapper.MovieOutboxDataAccessMapper;
import com.lintang.netflik.movieservice.outbox.mapper.MovieOutboxMessageMapper;
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
    private MovieOutboxMessageMapper movieOutboxMessageMapper;

    @Transactional
    public MovieOutboxMessage createMovieOutboxMessage(AddMovieEvent movieEvent) {

        log.info("movie created with id: {}", movieEvent.getId());

       return movieOutboxDataAccessMapper.movieOutboxEntityToMovieOutboxMessage( movieOutboxRepository.save(movieOutboxDataAccessMapper.
                        createMovieOutboxMessageEntity(movieOutboxMessageMapper
                                .createMovieEventToOutboxMessage(movieEvent))));

    }

    @Transactional
    public MovieOutboxMessage notificationMovieOutboxMessage(AddMovieEvent movieEvent) {
        log.info("movie event saved with id: {}", movieEvent.getId());
        return movieOutboxDataAccessMapper.movieOutboxEntityToMovieOutboxMessage(
                movieOutboxRepository.save(
                        movieOutboxDataAccessMapper.
                                notificationMovieOutboxMessageToEntity(
                                        movieOutboxMessageMapper.notificationMovieEventToOutboxMessage(movieEvent)
                                )));
    }

    @Transactional
    public MovieOutboxMessage updateMovieOutboxMessage(AddMovieEvent movieEvent) {
        log.info("movie updated with id: {}", movieEvent.getId());

        return movieOutboxDataAccessMapper.movieOutboxEntityToMovieOutboxMessage(
                movieOutboxRepository.save(movieOutboxDataAccessMapper.
                    updateMovieOutboxMessageToEntity(movieOutboxMessageMapper
                            .updateMovieEventToOutboxMessage(movieEvent))));
    }



    @Transactional
    public MovieOutboxMessage deleteMovieOutboxMessage(AddMovieEvent movieEvent) {
        log.info("movie deleted with id: {}", movieEvent.getId());

        return movieOutboxDataAccessMapper.movieOutboxEntityToMovieOutboxMessage( movieOutboxRepository.save(movieOutboxDataAccessMapper.
                deleteMovieOutboxMessageEntity(
                        movieOutboxMessageMapper.deleteMovieEvenToOutboxMessage(movieEvent)
                ) ));
    }

    public Optional<List<MovieOutboxMessage>> getMovieOutboxMessageByOutboxStatusAndType(OutboxStatus outboxStatus, String type) {


        Optional<List<MovieOutboxMessage>> movieOutboxMessageList =
                Optional.of(
                movieOutboxDataAccessMapper.toListMovieOutboxMessage(
                movieOutboxRepository.findByOutboxStatusAndType(outboxStatus, type).get()));
        // scheduler nya ada 3 create_movie, update_movie sama delete_movie dijalankan di waktu bersaamaan, pas di update movie id 31 versinya jadi 2 padahal di scheduler create versinya 1 jadi muncul error optimistic locking
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
