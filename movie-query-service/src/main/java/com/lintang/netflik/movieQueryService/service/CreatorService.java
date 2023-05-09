package com.lintang.netflik.movieQueryService.service;


import com.lintang.netflik.movieQueryService.dto.AddCreatorReq;
import com.lintang.netflik.movieQueryService.entity.CreatorEntity;
import com.lintang.netflik.movieQueryService.helper.entityMapper.CreatorEntityMapper;
import com.lintang.netflik.movieQueryService.repository.CreatorRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class CreatorService {
    private CreatorRepository repository;
    private CreatorEntityMapper mapper;

    public Optional<CreatorEntity> getByCreatorId(@Valid @NotNull int creatorId) {
        Optional<CreatorEntity> creator = repository.findById(creatorId);

        return creator;
    }


}
