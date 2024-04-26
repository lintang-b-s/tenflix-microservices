package com.kafkastreams.movieservice.api.server;


import com.kafkastreams.movieservice.api.request.AddActorReq;
import com.kafkastreams.movieservice.api.response.Actor;
import com.kafkastreams.movieservice.command.service.ActorCommandService;
import com.kafkastreams.movieservice.util.DtoMapper.ActorDtoMapper;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/v1/movie-service/actors")
public class ActorController {
    private static final Logger log = LoggerFactory.getLogger(ActorController.class);
    private ActorCommandService actorService;
    private ActorDtoMapper mapper;


    @Autowired
    public ActorController(ActorCommandService actorService, ActorDtoMapper mapper) {
        this.actorService = actorService;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<Actor> addActor(@RequestBody AddActorReq newActor) {
        return ok(mapper.actorEntityToActorDto(actorService.addActor(newActor)));
    }
//


    @PutMapping("/{actorId}")
    public ResponseEntity<Actor> updateActor(@PathVariable int actorId,@RequestBody AddActorReq newActor) {
        return actorService.updateActor(actorId, newActor).map(mapper::actorEntityToActorDto).map(ResponseEntity::ok)
                .orElse(notFound().build());
    }

}
