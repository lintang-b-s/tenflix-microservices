package com.lintang.netflik.movieservice.util.entityMapper;

import com.lintang.netflik.movieservice.api.response.Actor;
import com.lintang.netflik.movieservice.api.request.AddActorReq;
import com.lintang.netflik.movieservice.entity.ActorEntity;
import org.springframework.stereotype.Component;

@Component
public class ActorEntityMapper {

    public ActorEntity toEntity(AddActorReq m) {
        ActorEntity entity = new ActorEntity();
        return entity.setName(m.getName());
    }
    public ActorEntity toActorEntity(Actor m){
        ActorEntity entity = new ActorEntity();
        return entity.setName(m.getName()).setId(m.getId());
    }

    public ActorEntity toEntity(String m) {
        ActorEntity entity = new ActorEntity();
        return entity.setName(m);
    }
}
