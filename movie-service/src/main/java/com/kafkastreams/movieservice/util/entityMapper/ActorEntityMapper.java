package com.kafkastreams.movieservice.util.entityMapper;

import com.kafkastreams.movieservice.api.response.Actor;
import com.kafkastreams.movieservice.api.request.AddActorReq;
import com.kafkastreams.movieservice.entity.ActorEntity;
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
