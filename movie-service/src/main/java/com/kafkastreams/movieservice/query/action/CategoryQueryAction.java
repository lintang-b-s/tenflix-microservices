package com.kafkastreams.movieservice.query.action;


import com.kafkastreams.movieservice.entity.CategoryEntity;
import com.kafkastreams.movieservice.entity.CreatorEntity;
import com.kafkastreams.movieservice.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CategoryQueryAction {

    private CategoryRepository categoryRepository;


    @Autowired
    public CategoryQueryAction(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Optional<CategoryEntity> findByName(String name){
        return categoryRepository.findByName(name);
    }
}
