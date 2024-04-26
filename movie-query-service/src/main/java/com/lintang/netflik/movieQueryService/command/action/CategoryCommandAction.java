package com.lintang.netflik.movieQueryService.command.action;


import com.lintang.netflik.movieQueryService.broker.message.Category;
import com.lintang.netflik.movieQueryService.broker.message.UpdateCategoryMessage;
import com.lintang.netflik.movieQueryService.entity.CategoryEntity;
import com.lintang.netflik.movieQueryService.repository.MovieRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;



@Component
public class CategoryCommandAction {

    private MovieRepository movieRepository;

    private MongoTemplate mongoTemplate;

    @Autowired
    public CategoryCommandAction(
        MovieRepository movieRepository,
        MongoTemplate mongoTemplate
    ) {
        this.movieRepository = movieRepository;
        this.mongoTemplate = mongoTemplate;
    }



    public void updateCategory(UpdateCategoryMessage message) {
        Set<CategoryEntity> oldCategories = new HashSet<>();
        CategoryEntity oldCategory = CategoryEntity.builder().id(message.getId())
                .name(message.getOldName())
                .build();
        CategoryEntity newCategory = CategoryEntity.builder().id(message.getId())
                .name(message.getName())
                .build();
        oldCategories.add(oldCategory);
        Query query = new Query()
                .addCriteria(Criteria.where("movies.categories").in(oldCategories));
        Update updatePull = new Update().pull("movies.$.categories", oldCategory);
        Update updatePush = new Update().push("movies.$.categories", newCategory);
        mongoTemplate.updateMulti(query, updatePull, CategoryEntity.class);
        mongoTemplate.updateMulti(query, updatePull, CategoryEntity.class);


    }

}
