package com.lintang.netflik.movieservice.command.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lintang.netflik.movieservice.api.request.AddActorReq;
import com.lintang.netflik.movieservice.broker.message.UpdateActorMessage;
import com.lintang.netflik.movieservice.broker.message.UpdateCreatorMessage;
import com.lintang.netflik.movieservice.command.action.ActorCommandAction;
import com.lintang.netflik.movieservice.command.action.MovieOutboxAction;
import com.lintang.netflik.movieservice.entity.ActorEntity;
import com.lintang.netflik.movieservice.entity.OutboxEntity;
import com.lintang.netflik.movieservice.entity.OutboxEventType;
import com.lintang.netflik.movieservice.exception.InternalServerEx;
import com.lintang.netflik.movieservice.util.entityMapper.ActorEntityMapper;
import com.lintang.netflik.movieservice.repository.ActorRepository;
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
@AllArgsConstructor
@NoArgsConstructor
@Transactional
public class ActorCommandService {
    private static final Logger LOG = LoggerFactory.getLogger(ActorCommandService.class);

    @Autowired
    private ActorCommandAction actorCommandAction;
    @Autowired
    private MovieOutboxAction outboxAction;


    public ActorEntity addActor(@Valid AddActorReq newActor) {
        ActorEntity actor=  actorCommandAction.addActor(newActor);
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
        try{
            actorOutbox = outboxAction.insertOutbox(
                    "actor.request",
                    String.valueOf(actorId),
                    OutboxEventType.UPDATE_ACTOR, message

            );
        }catch (JsonProcessingException e) {
            throw new InternalServerEx("error json processing : " + e.getMessage());
        }
        outboxAction.deleteOutbox(actorOutbox);
        LOG.info("sending update_actor message with  id +  "+ String.valueOf(actorId) + " to movie-query-service!!" );

        return actor;
    }



}
