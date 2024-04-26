package com.kafkastreams.movieservice.command.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kafkastreams.movieservice.api.request.AddActorReq;
import com.kafkastreams.movieservice.broker.message.UpdateActorMessage;
import com.kafkastreams.movieservice.command.action.ActorCommandAction;
import com.kafkastreams.movieservice.command.action.MovieOutboxAction;
import com.kafkastreams.movieservice.entity.ActorEntity;
import com.kafkastreams.movieservice.entity.OutboxEntity;
import com.kafkastreams.movieservice.entity.OutboxEventType;
import com.kafkastreams.movieservice.exception.InternalServerEx;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Slf4j
@Service
@NoArgsConstructor
@Transactional
public class ActorCommandService {
    private static final Logger LOG = LoggerFactory.getLogger(ActorCommandService.class);

    private ActorCommandAction actorCommandAction;

    private MovieOutboxAction outboxAction;

    @Autowired
    public ActorCommandService(ActorCommandAction actorCommandAction, MovieOutboxAction outboxAction) {
        this.actorCommandAction = actorCommandAction;
        this.outboxAction = outboxAction;
    }

    public ActorEntity addActor(@Valid AddActorReq newActor) {
        ActorEntity actor = actorCommandAction.addActor(newActor);
        return actor;
    }

    public Optional<ActorEntity> getByActorId(@Valid @NotNull int id) {
        Optional<ActorEntity> actor = actorCommandAction.getByActorId(id);
        return actor;
    }

    public Optional<ActorEntity> updateActor(int actorId, AddActorReq newActor) {
        Optional<ActorEntity> actor = actorCommandAction.updateActor(actorId, newActor);
        UpdateActorMessage message = UpdateActorMessage.builder()
                .id(actorId).name(newActor.getName())
                .build();
        OutboxEntity actorOutbox = null;
        try {
            actorOutbox = outboxAction.insertOutbox(
                    "actor.request",
                    String.valueOf(actorId),
                    OutboxEventType.UPDATE_ACTOR, message

            );
        } catch (JsonProcessingException e) {
            throw new InternalServerEx("error json processing : " + e.getMessage());
        }
        outboxAction.deleteOutbox(actorOutbox);
        LOG.info("sending update_actor message with  id +  " + String.valueOf(actorId) + " to movie-query-service!!");

        return actor;
    }

}
