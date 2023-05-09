package com.lintang.netflik.movieQueryService.service;

import com.lintang.netflik.movieQueryService.dto.AddActorReq;
import com.lintang.netflik.movieQueryService.entity.ActorEntity;
import com.lintang.netflik.movieQueryService.helper.entityMapper.ActorEntityMapper;
import com.lintang.netflik.movieQueryService.repository.ActorRepository;
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

    public Optional<ActorEntity> getByActorId(@Valid @NotNull int id) {
        Optional<ActorEntity> actor = repository.findById(id);

        return actor;
    }

}
