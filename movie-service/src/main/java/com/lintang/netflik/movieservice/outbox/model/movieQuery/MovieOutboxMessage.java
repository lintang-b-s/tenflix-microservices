package com.lintang.netflik.movieservice.outbox.model.movieQuery;


import com.lintang.netflik.movieservice.outbox.model.OutboxStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MovieOutboxMessage {
    private UUID id;
    private ZonedDateTime createdAt;
    private ZonedDateTime processedAt;
    private String type;
    private String payload;
    private OutboxStatus outboxStatus;
    private int version;


    public UUID getId() {
        return id;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public ZonedDateTime getProcessedAt() {
        return processedAt;
    }

    public String getType() {
        return type;
    }

    public String getPayload() {
        return payload;
    }

    public OutboxStatus getOutboxStatus() {
        return outboxStatus;
    }

    public int getVersion() {
        return version;
    }

    public MovieOutboxMessage setProcessedAt(ZonedDateTime processedAt) {
        this.processedAt = processedAt;
        return this;
    }

    public MovieOutboxMessage setOutboxStatus(OutboxStatus outboxStatus) {
        this.outboxStatus = outboxStatus;
        return this;
    }

    public MovieOutboxMessage setId(UUID id) {
        this.id = id;
        return this;
    }

    public MovieOutboxMessage setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public MovieOutboxMessage setType(String type) {
        this.type = type;
        return this;
    }

    public MovieOutboxMessage setPayload(String payload) {
        this.payload = payload;
        return this;
    }

    public MovieOutboxMessage setVersion(int version) {
        this.version = version;
        return this;
    }
}
