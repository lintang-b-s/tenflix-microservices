package com.kafkastreams.movieservice.query.action;

import com.kafkastreams.movieservice.entity.TagEntity;
import com.kafkastreams.movieservice.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TagQueryAction {

    private TagRepository tagRepository;

    @Autowired
    public TagQueryAction(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public Optional<TagEntity> findById(int id){
        return tagRepository.findById(id);
    }
}
