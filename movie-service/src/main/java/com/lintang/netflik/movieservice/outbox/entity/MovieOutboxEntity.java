package com.lintang.netflik.movieservice.outbox.entity;

import com.lintang.netflik.movieservice.outbox.model.OutboxStatus;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "movie_outbox")
@Entity
public class MovieOutboxEntity {

    @Id
    private UUID id;
    private ZonedDateTime createdAt;
    private ZonedDateTime processedAt;
    private String type; //add, update, delete, notification
    private String payload; // ganti text di postgres nya
    @Enumerated(EnumType.STRING)
    private OutboxStatus outboxStatus;
    @Version
    private  int version;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovieOutboxEntity that = (MovieOutboxEntity) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public UUID getId() {
        return id;
    }

    public MovieOutboxEntity setId(UUID id) {
        this.id = id;
        return this;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public MovieOutboxEntity setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public ZonedDateTime getProcessedAt() {
        return processedAt;
    }

    public MovieOutboxEntity setProcessedAt(ZonedDateTime processedAt) {
        this.processedAt = processedAt;
        return this;
    }

    public String getType() {
        return type;
    }

    public MovieOutboxEntity setType(String type) {
        this.type = type;
        return this;
    }

    public String getPayload() {
        return payload;
    }

    public MovieOutboxEntity setPayload(String payload) {
        this.payload = payload;
        return this;
    }

    public OutboxStatus getOutboxStatus() {
        return outboxStatus;
    }

    public MovieOutboxEntity setOutboxStatus(OutboxStatus outboxStatus) {
        this.outboxStatus = outboxStatus;
        return this;
    }

    public int getVersion() {
        return version;
    }

    public MovieOutboxEntity setVersion(int version) {
        this.version = version;
        return this;
    }
}
