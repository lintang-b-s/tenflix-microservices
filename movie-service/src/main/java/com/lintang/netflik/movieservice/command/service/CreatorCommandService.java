package com.lintang.netflik.movieservice.command.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.lintang.netflik.movieservice.api.request.AddCreatorReq;
import com.lintang.netflik.movieservice.broker.message.UpdateCreatorMessage;
import com.lintang.netflik.movieservice.broker.message.UpdateTagMessage;
import com.lintang.netflik.movieservice.command.action.CreatorCommandAction;
import com.lintang.netflik.movieservice.command.action.MovieOutboxAction;
import com.lintang.netflik.movieservice.entity.CreatorEntity;
import com.lintang.netflik.movieservice.entity.OutboxEntity;
import com.lintang.netflik.movieservice.entity.OutboxEventType;
import com.lintang.netflik.movieservice.exception.InternalServerEx;
import com.lintang.netflik.movieservice.util.entityMapper.CreatorEntityMapper;
import com.lintang.netflik.movieservice.repository.CreatorRepository;
import lombok.AllArgsConstructor;
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
@Transactional
@AllArgsConstructor
public class CreatorCommandService {

    private static final Logger LOG = LoggerFactory.getLogger(CreatorCommandService.class);


    @Autowired
    private CreatorCommandAction creatorCommandAction;


    @Autowired
    private MovieOutboxAction outboxAction;


    @Transactional
    public CreatorEntity addCreator(@Valid AddCreatorReq newCreator) {
        return creatorCommandAction.addCreator(newCreator);
    }

    @Transactional
    public CreatorEntity getByCreatorId(@Valid @NotNull int creatorId) {
       CreatorEntity creatorEntity = creatorCommandAction.getCreatorById(creatorId);
       return creatorEntity;
    }


    @Transactional
    public Optional<CreatorEntity> updateCreator(int creatorId , AddCreatorReq newCreator) {
        CreatorEntity oldCreator = creatorCommandAction.getCreatorById(creatorId);
        Optional<CreatorEntity> creator = creatorCommandAction.updateCreator(creatorId, newCreator);
        UpdateCreatorMessage message = UpdateCreatorMessage.builder()
                .id(creatorId).name(newCreator.getName()).oldName(oldCreator.getName())
                .build();
        OutboxEntity creatorOutbox = null;
        try{
            creatorOutbox = outboxAction.insertOutbox(
                    "creator.request",
                    String.valueOf(creatorId),
                    OutboxEventType.UPDATE_CREATOR, message
            );
        }catch (JsonProcessingException e) {
            throw new InternalServerEx("error json processing : " + e.getMessage());
        }
        outboxAction.deleteOutbox(creatorOutbox);
        LOG.info("sending update_creator message with  id +  "+ String.valueOf(creatorId) + " to movie-query-service!!" );


        return creator;
    }
}
