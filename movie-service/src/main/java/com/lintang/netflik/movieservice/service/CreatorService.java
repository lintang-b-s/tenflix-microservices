package com.lintang.netflik.movieservice.service;


import com.lintang.netflik.movieservice.dto.AddCreatorReq;
import com.lintang.netflik.movieservice.entity.CreatorEntity;
import com.lintang.netflik.movieservice.helper.entityMapper.CreatorEntityMapper;
import com.lintang.netflik.movieservice.repository.CreatorRepository;
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

    public CreatorEntity addCreator(@Valid AddCreatorReq newCreator) {
        return repository.save(mapper.toEntity(newCreator));
    }
    public Optional<CreatorEntity> getByCreatorId(@Valid @NotNull int creatorId) {
        Optional<CreatorEntity> creator = repository.findById(creatorId);

        return creator;
    }

    public Optional<CreatorEntity> updateCreator(int creatorId , AddCreatorReq newCreator) {
        Optional<CreatorEntity> creator = repository.findById(creatorId);
        if (!creator.isPresent()) {
            log.error("creator not found");
        }
        CreatorEntity updatedCreator = creator.get();
        updatedCreator.setName(newCreator.getName());
        return Optional.of(repository.save(updatedCreator));
    }
}
