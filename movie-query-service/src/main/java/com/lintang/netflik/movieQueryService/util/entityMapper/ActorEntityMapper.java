package com.lintang.netflik.movieQueryService.util.entityMapper;

import com.lintang.netflik.movieQueryService.broker.message.Actor;
import com.lintang.netflik.movieQueryService.entity.ActorEntity;
import org.springframework.stereotype.Component;

@Component
public class ActorEntityMapper {

    public ActorEntity toEntity(Actor m) {
        ActorEntity entity = new ActorEntity();
        return entity.setName(m.getName());
    }
    public ActorEntity toActorEntity(Actor m){
        ActorEntity entity = new ActorEntity();
        return entity.setName(m.getName()).setId(m.getId());
    }
}
