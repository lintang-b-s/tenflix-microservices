package com.lintang.netflik.movieservice.command.action;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.lintang.netflik.movieservice.entity.OutboxEntity;
import com.lintang.netflik.movieservice.repository.OutboxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MovieOutboxAction {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OutboxRepository outboxRepository;
    public void deleteOutbox(OutboxEntity outbox) {
        outboxRepository.delete(outbox);
    }
    public OutboxEntity insertOutbox(OutboxEntity cqrsOutbox) {
        OutboxEntity saved= outboxRepository.save(cqrsOutbox);
        return saved;
    }

    public OutboxEntity insertOutbox(String aggregateType, String aggregateId, String type, Object payload)
            throws JsonProcessingException {
        var outbox = new OutboxEntity();

        outbox.setAggregateType(aggregateType);
        outbox.setAggregateId(aggregateId);
        outbox.setType(type);
        outbox.setPayload(objectMapper.writeValueAsString(payload)); //error nya disini

        return insertOutbox(outbox);
    }


}
