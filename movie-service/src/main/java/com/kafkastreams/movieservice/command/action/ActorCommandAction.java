package com.kafkastreams.movieservice.command.action;


import com.kafkastreams.movieservice.api.request.AddActorReq;
import com.kafkastreams.movieservice.api.response.Actor;
import com.kafkastreams.movieservice.entity.ActorEntity;
import com.kafkastreams.movieservice.exception.BadRequestException;
import com.kafkastreams.movieservice.exception.ResourceNotFoundException;
import com.kafkastreams.movieservice.repository.ActorRepository;
import com.kafkastreams.movieservice.util.entityMapper.ActorEntityMapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@NoArgsConstructor
public class ActorCommandAction {

    private ActorRepository repository;

    private ActorEntityMapper mapper;



    @Autowired
    public ActorCommandAction(ActorRepository repository, ActorEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public ActorEntity addActor(AddActorReq newActor) {
        Optional<ActorEntity> actorInDb = repository.findByName(newActor.getName());
        if (!actorInDb.isEmpty()){
            throw new BadRequestException("actor with name: " + newActor.getName() + " already in database");
        }
        ActorEntity actor =  repository.save(mapper.toEntity(newActor));
        return actor;
    }

    public ActorEntity saveActor(Actor actorReq){
        ActorEntity actornew = mapper.toEntity(actorReq.getName());
        ActorEntity actor =  repository.save(actornew);
        return actor;
    }

    public Optional<ActorEntity> getByActorId( int id) {
        Optional<ActorEntity> actor = repository.findById(id);

        return actor;
    }

    public Optional<ActorEntity> updateActor(int actorId, AddActorReq newActor) {
        Optional<ActorEntity> actor = repository.findById(actorId);
        if (!actor.isPresent()) {
          throw  new ResourceNotFoundException("actor with id: " + actorId + " not found");
        }
        ActorEntity updatedActor = actor.get();
        updatedActor.setName(newActor.getName());
        return Optional.of(repository.save(updatedActor));
    }

}
