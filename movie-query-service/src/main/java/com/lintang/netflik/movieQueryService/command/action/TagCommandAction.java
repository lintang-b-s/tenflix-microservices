package com.lintang.netflik.movieQueryService.command.action;


import com.lintang.netflik.movieQueryService.broker.message.UpdateTagMessage;
import com.lintang.netflik.movieQueryService.entity.MovieEntity;
import com.lintang.netflik.movieQueryService.entity.TagEntity;
import com.lintang.netflik.movieQueryService.repository.MovieRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;


import java.util.HashSet;
import java.util.Set;

import static com.mongodb.client.model.Filters.where;


@AllArgsConstructor
@NoArgsConstructor
@Component
public class TagCommandAction {

    private MovieRepository movieRepository;
    private  MongoTemplate mongoTemplate;
    public void updateTag(UpdateTagMessage message) {
        Set<TagEntity> oldTags = new HashSet<>();
        TagEntity oldTag = TagEntity.builder().id(message.getId()).name(message.getOldname()).build();
        TagEntity newTag = TagEntity.builder().id(message.getId()).name(message.getName()).build();
        oldTags.add(oldTag);
        Query query = new Query()
                .addCriteria(Criteria.where("movies.tags").in(oldTags));
        Update update = new Update().pull("movies.$.tags", oldTag);
        Update updatePush = new Update().push("movies.$.tags", newTag);

        mongoTemplate.updateMulti(query, update , TagEntity.class);
        mongoTemplate.updateMulti(query, updatePush, TagEntity.class);
        return ;

    }


}
