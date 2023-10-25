package com.kafkastreams.movieservice.command.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kafkastreams.movieservice.command.action.CategoryCommandAction;
import com.kafkastreams.movieservice.command.action.MovieOutboxAction;
import com.kafkastreams.movieservice.api.request.AddCategoryReq;
import com.kafkastreams.movieservice.broker.message.UpdateCategoryMessage;
import com.kafkastreams.movieservice.entity.CategoryEntity;
import com.kafkastreams.movieservice.entity.OutboxEntity;
import com.kafkastreams.movieservice.entity.OutboxEventType;
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
public class CategoryCommandService {
    private static final Logger LOG = LoggerFactory.getLogger(CategoryCommandService.class);

    private CategoryCommandAction categoryCommandAction;

    private MovieOutboxAction outboxAction;

    @Autowired
    public CategoryCommandService(CategoryCommandAction categoryCommandAction, MovieOutboxAction outboxAction) {
        this.categoryCommandAction = categoryCommandAction;
        this.outboxAction = outboxAction;
    }

    @Transactional
    public CategoryEntity addCategory(@Valid AddCategoryReq newCategory) {
        CategoryEntity cat = categoryCommandAction.saveReq(newCategory);
        return cat;
    }

    @Transactional
    public CategoryEntity updateCategory(int categoryId, @Valid AddCategoryReq newCategory) {
        CategoryEntity oldCat = categoryCommandAction.findById(categoryId);
        CategoryEntity cat = categoryCommandAction.update(categoryId, newCategory);
        UpdateCategoryMessage message = UpdateCategoryMessage.builder()
                .id(categoryId).name(newCategory.getName()).oldName(oldCat.getName())
                .build();
        OutboxEntity categoryOutbox = null;
        try {
            categoryOutbox = outboxAction.insertOutbox(
                    "category.request",
                    String.valueOf(categoryId),
                    OutboxEventType.UPDATE_CATEGORY, message);
        } catch (JsonProcessingException e) {
            throw new InternalServerEx("error json processing : " + e.getMessage());
        }
        outboxAction.deleteOutbox(categoryOutbox);
        LOG.info("sending update_category message with  id +  " + String.valueOf(categoryId)
                + " to movie-query-service!!");

        return cat;
    }

}
