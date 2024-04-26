package com.lintang.netflik.movieQueryService.command.action;


import com.lintang.netflik.movieQueryService.broker.message.UpdateCreatorMessage;
import com.lintang.netflik.movieQueryService.entity.CreatorEntity;
import com.lintang.netflik.movieQueryService.repository.MovieRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class CreatorCommandAction {
    private MongoTemplate mongoTemplate;
    private MovieRepository movieRepository;

    @Autowired
    public CreatorCommandAction(
        MongoTemplate  mongoTemplate,
        MovieRepository movieRepository
    ) {
        this.mongoTemplate = mongoTemplate;
        this.movieRepository = movieRepository;
    }

    public void updateCreator(UpdateCreatorMessage message) {
        Set<CreatorEntity> oldCreators = new HashSet<>();
        CreatorEntity oldCreator = CreatorEntity.builder().id(message.getId())
                .name(message.getOldName())
                .build();
        CreatorEntity newCreator = CreatorEntity.builder().id(message.getId())
                .name(message.getName())
                .build();
        oldCreators.add(oldCreator);
        Query query = new Query()
                .addCriteria(Criteria.where("movies.creators").in(oldCreators));
        Update updatePull = new Update().pull("movies.$.creators", oldCreator);
        Update updatePush = new Update().push("movies.$.creators", newCreator);

        mongoTemplate.updateMulti(query, updatePull, CreatorEntity.class);
        mongoTemplate.updateMulti(query, updatePush, CreatorEntity.class);
        return ;

    }

}
