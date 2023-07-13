package com.lintang.netflik.subscriptionservice.entity;


import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name ="order_outbox")
public class OutboxEntity {

    @Column(nullable = false, length = 255, name = "aggregateid")
    private String aggregateId;

    @Column(nullable = false, length = 255, name = "aggregatetype")
    private String aggregateType;

    @CreationTimestamp
    private LocalDateTime createdTimestamp;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "outbox_seq")
    @SequenceGenerator(name = "outbox_seq")
    private long id;

    //    di postman debezium connector nya tambahin di reqbody:
    // "table.fields.additional.placement":"sagaStatus:envelope"
    @Column(nullable = false, length = 255, name = "sagastatus")
    private String sagaStatus;

    /**
     * JSON structure with the actual event contents
     */
    @Column(nullable = false, length = 4000)
    private String payload;

    @Column(nullable = false, length = 255)
    private String type;

    public String getAggregateId() {
        return aggregateId;
    }

    public void setAggregateId(String aggregateId) {
        this.aggregateId = aggregateId;
    }

    public String getAggregateType() {
        return aggregateType;
    }

    public void setAggregateType(String aggregateType) {
        this.aggregateType = aggregateType;
    }

    public LocalDateTime getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(LocalDateTime createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getString() {
        return sagaStatus;
    }

    public void setSagaStatus(String sagaStatus) {
        this.sagaStatus = sagaStatus;
    }
}
