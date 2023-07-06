package com.lintang.netflik.movieservice.outbox.scheduler;

import com.lintang.netflik.movieservice.outbox.MovieOutboxHelper;
import com.lintang.netflik.movieservice.outbox.model.OutboxStatus;
import com.lintang.netflik.movieservice.outbox.model.movieQuery.MovieOutboxMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// kurang scheduler clceaner ini sama outbox notification
@Slf4j
@Component
@AllArgsConstructor
public class MovieOutboxCleanerScheduler {
    private final MovieOutboxHelper movieOutboxHelper;


    @Scheduled(cron = "@midnight")
    public void proccessOutboxMessage() {
        Optional<List<MovieOutboxMessage>> outboxMessagesResponse =
                movieOutboxHelper.getMovieOutboxMessageByOutboxStatus(OutboxStatus.COMPLETED);
        if (outboxMessagesResponse.isPresent()) {
            List<MovieOutboxMessage> outboxMessages = outboxMessagesResponse.get();
            log.info("Received {} MovieOutboxxMessage for clean up. The payloads: {}",
                    outboxMessages.size(),
                    outboxMessages.stream().map(MovieOutboxMessage::getPayload)
                            .collect(Collectors.joining("\n")));
            movieOutboxHelper.deleteMovieOutboxMessageByOutboxStatus(
                    OutboxStatus.COMPLETED
            );
            log.info("{} MovieOutboxMesssage deleted", outboxMessages.size());
        }

    }

}
