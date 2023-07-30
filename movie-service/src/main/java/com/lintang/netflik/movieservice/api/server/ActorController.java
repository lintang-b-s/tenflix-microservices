package com.lintang.netflik.movieservice.api.server;


import com.lintang.netflik.movieservice.api.response.Actor;
import com.lintang.netflik.movieservice.api.request.AddActorReq;
import com.lintang.netflik.movieservice.command.service.ActorCommandService;
import com.lintang.netflik.movieservice.util.DtoMapper.ActorDtoMapper;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/v1/movie-service/actors")
@AllArgsConstructor
@PreAuthorize("hasAuthority('ROLE_default-roles-tenflix')")
public class ActorController {
    private static final Logger log = LoggerFactory.getLogger(ActorController.class);
    private ActorCommandService actorService;
    private ActorDtoMapper mapper;


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
