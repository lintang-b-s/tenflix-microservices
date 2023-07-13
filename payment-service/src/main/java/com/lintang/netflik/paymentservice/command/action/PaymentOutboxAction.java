package com.lintang.netflik.paymentservice.command.action;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.lintang.netflik.paymentservice.entity.OutboxEntity;
import com.lintang.netflik.paymentservice.entity.SagaStatus;
import com.lintang.netflik.paymentservice.repository.OutboxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PaymentOutboxAction {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OutboxRepository outboxRepository;
    public void deleteOutbox(OutboxEntity outbox) {
        outboxRepository.delete(outbox);
    }
    public OutboxEntity insertOutbox(OutboxEntity outbox) {
        OutboxEntity saved=  outboxRepository.save(outbox);
        return saved;
    }

    public OutboxEntity insertOutbox(String aggregateType, String aggregateId, String type, Object payload,
                                     String sagaStatus)
            throws JsonProcessingException {
        var outbox = new OutboxEntity();

        outbox.setAggregateType(aggregateType);
        outbox.setAggregateId(aggregateId);
        outbox.setType(type);
        outbox.setPayload(objectMapper.writeValueAsString(payload));
        outbox.setSagaStatus(sagaStatus);

        return insertOutbox(outbox);
    }


}
