package com.kafkastreams.movieservice.query.action;


import com.kafkastreams.movieservice.entity.ActorEntity;
import com.kafkastreams.movieservice.repository.ActorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ActorQueryAction {

    private ActorRepository actorRepository;

    @Autowired
    public ActorQueryAction(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }

    public Optional<ActorEntity> findActor(String  name){

        return actorRepository.findByName(name);

    }
}
