package com.lintang.netflik.movieQueryService.controller;


import com.lintang.netflik.movieQueryService.dto.Actor;
import com.lintang.netflik.movieQueryService.dto.AddActorReq;
import com.lintang.netflik.movieQueryService.helper.DtoMapper.ActorDtoMapper;
import com.lintang.netflik.movieQueryService.service.ActorService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/v1/movie-query/actors")
@AllArgsConstructor
@PreAuthorize("hasAuthority('ROLE_user')")
public class ActorController {
    private static final Logger log = LoggerFactory.getLogger(ActorController.class);
    private ActorService actorService;
    private ActorDtoMapper mapper;



    @GetMapping("/{actorId}")
    public ResponseEntity<Actor> getByActorId(@PathVariable  int actorId) {
        return actorService.getByActorId(actorId).map(mapper::actorEntityToActorDto).map(ResponseEntity::ok)
                .orElse(notFound().build());
    }


}
