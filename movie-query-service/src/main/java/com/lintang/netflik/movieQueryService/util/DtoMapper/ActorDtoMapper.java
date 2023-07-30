package com.lintang.netflik.movieQueryService.util.DtoMapper;

import com.lintang.netflik.movieQueryService.entity.ActorEntity;
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
