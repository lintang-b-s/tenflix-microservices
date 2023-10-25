package com.kafkastreams.movieservice.query.action;


import com.kafkastreams.movieservice.entity.VideoEntity;
import com.kafkastreams.movieservice.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class VideoQueryAction {
    private VideoRepository videoRepository;

    @Autowired
    public VideoQueryAction(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    public Optional<VideoEntity> findById(int id){
        return videoRepository.findById(id);
    }

}
