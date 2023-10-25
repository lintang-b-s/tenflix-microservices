package com.kafkastreams.movieservice.query.action;

import com.kafkastreams.movieservice.entity.CreatorEntity;
import com.kafkastreams.movieservice.repository.CreatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CreatorQueryAction {

    private CreatorRepository creatorRepository;


    @Autowired
    public CreatorQueryAction(CreatorRepository creatorRepository) {
        this.creatorRepository = creatorRepository;
    }

    public Optional<CreatorEntity> findByName(String  name){
        return creatorRepository.findByName(name);
    }
}
