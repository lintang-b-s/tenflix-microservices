package com.lintang.netflik.movieservice.outbox.entity;

import com.lintang.netflik.movieservice.outbox.model.OutboxStatus;
import lombok.*;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.Objects;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "movie_outbox")
@Entity
public class MovieOutboxEntity {

    @Id
    private int id;
    private ZonedDateTime createdAt;
    private ZonedDateTime processedAt;
    private String type; //add, update, delete, notification
    private String payload;
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
}
