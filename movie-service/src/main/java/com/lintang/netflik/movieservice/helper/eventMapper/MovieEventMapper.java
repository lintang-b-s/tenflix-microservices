package com.lintang.netflik.movieservice.helper.eventMapper;


import com.lintang.netflik.movieservice.dto.Actor;
import com.lintang.netflik.movieservice.dto.Creator;
import com.lintang.netflik.movieservice.dto.Video;
import com.lintang.netflik.movieservice.entity.ActorEntity;
import com.lintang.netflik.movieservice.entity.CreatorEntity;
import com.lintang.netflik.movieservice.entity.MovieEntity;
import com.lintang.netflik.movieservice.entity.VideoEntity;
import com.lintang.netflik.movieservice.event.AddMovieEvent;
import com.lintang.netflik.movieservice.event.AddMovieEventPayload;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.stream.Collectors;

@Component
public class MovieEventMapper {
    public AddMovieEvent movieEntityToAddMovieEvent(MovieEntity entity) {
        AddMovieEvent newMovieEvent = new AddMovieEvent();
        LocalDate entityLDT = entity.getrYear().toLocalDateTime().toLocalDate();

        return newMovieEvent.setId(entity.getId()).setName(entity.getName()).setType(entity.getType())
                 .setSynopsis(entity.getSynopsis()).setMpaRating(entity.getMpaRating()).setrYear(entityLDT)
                .setIdmbRating(entity.getIdmbRating()).setCreators(entity.getCreators().stream().map(m -> toCreatorModel(m)).collect(Collectors.toSet()))
                .setActors(entity.getActors().stream().map(m -> toActorModel(m)).collect(Collectors.toSet()))
                .setImage(entity.getImage()).setVideos(entity.getVideos().stream().map(m -> toVideoModel(m)).collect(Collectors.toSet()))
                .setCreatedAt(ZonedDateTime.now(ZoneId.of("UTC")));

    }

    public AddMovieEvent addMovieEventPayloadToAddMovieEvent(AddMovieEventPayload addMovieEventPayload) {
        AddMovieEvent newMovieEvent = new AddMovieEvent();
        LocalDate entityLDT = addMovieEventPayload.getRYear();

        return newMovieEvent.setId(addMovieEventPayload.getId()).setName(addMovieEventPayload.getName()).setType(addMovieEventPayload.getType())
                .setSynopsis(addMovieEventPayload.getSynopsis()).setMpaRating(addMovieEventPayload.getMpaRating()).setrYear(entityLDT)
                .setIdmbRating(addMovieEventPayload.getIdmbRating()).setCreators(addMovieEventPayload.getCreators())
                .setActors(addMovieEventPayload.getActors())
                .setImage(addMovieEventPayload.getImage()).setVideos(addMovieEventPayload.getVideos())
                .setOutboxType(addMovieEventPayload.getOutboxType());


    }

    private Actor toActorModel(ActorEntity actorEntity) {
        Actor m = new Actor();
        m.setId(actorEntity.getId());m.setName(actorEntity.getName());

        return m;
    }

    private Creator toCreatorModel(CreatorEntity creatorEntity) {
        Creator m = new Creator();
        m.setId(creatorEntity.getId());
        m.setName(creatorEntity.getName());

        return m;
    }

    private Video toVideoModel(VideoEntity videoEntity) {
        Video m =  new Video();
        m.setId(videoEntity.getId());
        m.setUrl(videoEntity.getUrl());
        m.setLength(videoEntity.getLength());m.setTitle(videoEntity.getTitle());
        m.setSynopsis(videoEntity.getSynopsis());
        return m;
    }
}
