package com.lintang.netflik.movieQueryService.controller;

import com.lintang.netflik.movieQueryService.dto.AddCreatorReq;
import com.lintang.netflik.movieQueryService.dto.Creator;
import com.lintang.netflik.movieQueryService.helper.DtoMapper.CreatorDtoMapper;
import com.lintang.netflik.movieQueryService.service.CreatorService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/query/v1/creators")
@AllArgsConstructor
public class CreatorController {
    private static final Logger log = LoggerFactory.getLogger(CreatorController.class);
    private CreatorService creatorService;
    private CreatorDtoMapper mapper;


    @GetMapping("/{creatorId}")
    public ResponseEntity<Creator> getByCreatorId(@PathVariable int creatorId) {
        return creatorService.getByCreatorId(creatorId).map(mapper::creatorEntityToCreatorDto)
                .map(ResponseEntity::ok).orElse(notFound().build());
    }


}
