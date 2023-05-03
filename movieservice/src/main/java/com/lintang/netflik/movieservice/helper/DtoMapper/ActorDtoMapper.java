package com.lintang.netflik.movieservice.helper.DtoMapper;

import com.lintang.netflik.movieservice.dto.Actor;
import com.lintang.netflik.movieservice.dto.Movie;
import com.lintang.netflik.movieservice.entity.ActorEntity;
import com.lintang.netflik.movieservice.entity.MovieEntity;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

@Component
public class ActorDtoMapper {
    public Actor actorEntityToActorDto(ActorEntity entity) {
        Actor actor = new Actor();
        actor.setId(entity.getId());actor.setName(entity.getName());
        return actor;
    }

    public List<Actor> toListModel(Iterable<ActorEntity> entities) {
        if (Objects.isNull(entities)) {
            return Collections.emptyList();
        }
        return StreamSupport.stream(entities.spliterator(), false).map(e -> actorEntityToActorDto(e))
                .collect(toList());
    }


}
