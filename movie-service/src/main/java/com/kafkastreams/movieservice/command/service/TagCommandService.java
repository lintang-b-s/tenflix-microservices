package com.kafkastreams.movieservice.command.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kafkastreams.movieservice.command.action.MovieOutboxAction;
import com.kafkastreams.movieservice.command.action.TagCommandAction;
import com.kafkastreams.movieservice.api.request.AddTagReq;
import com.kafkastreams.movieservice.broker.message.UpdateTagMessage;
import com.kafkastreams.movieservice.entity.OutboxEntity;
import com.kafkastreams.movieservice.entity.OutboxEventType;
import com.kafkastreams.movieservice.entity.TagEntity;
import com.kafkastreams.movieservice.exception.InternalServerEx;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;

@NoArgsConstructor
@Service
public class TagCommandService {

    private static final Logger LOG = LoggerFactory.getLogger(TagCommandService.class);
    private TagCommandAction tagCommandAction;
    private MovieOutboxAction outboxAction;

    @Autowired
    public TagCommandService(TagCommandAction tagCommandAction, MovieOutboxAction outboxAction) {
        this.tagCommandAction = tagCommandAction;
        this.outboxAction = outboxAction;
    }

    @Transactional
    public TagEntity addTag(@Valid AddTagReq newTag) {
        TagEntity tag = tagCommandAction.saveReqTag(newTag);
        return tag;
    }

    @Transactional
    public TagEntity updateTag(int tagId, @Valid AddTagReq newTag) {
        TagEntity oldTag = tagCommandAction.findById(tagId);
        TagEntity tag = tagCommandAction.update(tagId, newTag);
        UpdateTagMessage message = UpdateTagMessage.builder()
                .id(tagId).name(newTag.getName()).oldName(oldTag.getName())
                .build();
        OutboxEntity tagOutbox = null;
        try {
            tagOutbox = outboxAction.insertOutbox(
                    "tag.request",
                    String.valueOf(tagId),
                    OutboxEventType.UPDATE_TAG, message);
        } catch (JsonProcessingException e) {
            throw new InternalServerEx("error json processing : " + e.getMessage());
        }
        outboxAction.deleteOutbox(tagOutbox);
        LOG.info("sending update_tag message with  id +  " + String.valueOf(tagId) + " to movie-query-service!!");

        return tag;
    }

}
