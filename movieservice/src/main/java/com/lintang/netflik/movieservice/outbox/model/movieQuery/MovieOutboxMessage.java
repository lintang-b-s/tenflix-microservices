package com.lintang.netflik.movieservice.outbox.model.movieQuery;


import com.lintang.netflik.movieservice.outbox.model.OutboxStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
@Builder
@AllArgsConstructor
public class MovieOutboxMessage {
    private int id;
    private ZonedDateTime createdAt;
    private ZonedDateTime processedAt;
    private String type;
    private String payload;
    private OutboxStatus outboxStatus;

    private int version;


    public void setProcessedAt(ZonedDateTime processedAt) {
        this.processedAt = processedAt;
    }


    public void setOutboxStatus(OutboxStatus outboxStatus) {
        this.outboxStatus = outboxStatus;
    }


}
