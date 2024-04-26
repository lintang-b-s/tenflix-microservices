package com.lintang.netflik.movieQueryService.command.action;


import com.lintang.netflik.movieQueryService.broker.message.UpdateActorMessage;
import com.lintang.netflik.movieQueryService.entity.ActorEntity;
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
public class ActorCommandAction {

    private MovieRepository movieRepository;

    private MongoTemplate mongoTemplate;

    @Autowired
    public ActorCommandAction(
        MovieRepository movieRepository,
        MongoTemplate  mongoTemplate
    ){
        this.movieRepository = movieRepository;
        this.mongoTemplate = mongoTemplate;
    }



    public void updateActor(UpdateActorMessage message) {
        Set<ActorEntity> oldActors = new HashSet<>();
        ActorEntity oldActor = ActorEntity.builder().id(message.getId())
                .name(message.getName())
                .build();
        ActorEntity newActor = ActorEntity.builder().id(message.getId())
                .name(message.getName())
                .build();
        oldActors.add(oldActor);
        Query query = new Query()
                .addCriteria(Criteria.where("movies.actors").in(oldActors));
        Update updatePull = new Update().pull("movies.$.actors", oldActor);
        Update updatePush = new Update().push("movies.$.actors", newActor);

        mongoTemplate.updateMulti(query, updatePull, ActorEntity.class);
        mongoTemplate.updateMulti(query, updatePush, ActorEntity.class);

        return ;
    }

}
