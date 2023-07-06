package com.lintang.netflik.movieservice.service;

import com.lintang.netflik.movieservice.dto.AddActorReq;
import com.lintang.netflik.movieservice.entity.ActorEntity;
import com.lintang.netflik.movieservice.helper.entityMapper.ActorEntityMapper;
import com.lintang.netflik.movieservice.repository.ActorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class ActorService {
    private ActorRepository repository;
    private ActorEntityMapper mapper;


    public ActorService(ActorRepository repository,
                        ActorEntityMapper mapper) { this.repository = repository ;
    this.mapper = mapper;}


    public ActorEntity addActor(@Valid AddActorReq newActor) {
        ActorEntity actor =  repository.save(mapper.toEntity(newActor));
        return actor;
    }


    public Optional<ActorEntity> getByActorId(@Valid @NotNull int id) {
        Optional<ActorEntity> actor = repository.findById(id);

        return actor;
    }

    public Optional<ActorEntity> updateActor(int actorId, AddActorReq newActor) {
        Optional<ActorEntity> actor = repository.findById(actorId);
        if (!actor.isPresent()) {
            log.error("error");
        }
        ActorEntity updatedActor = actor.get();
        updatedActor.setName(newActor.getName());
        return Optional.of(repository.save(updatedActor));
    }



}
