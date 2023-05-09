package com.lintang.netflik.movieQueryService.helper.entityMapper;

import com.lintang.netflik.movieQueryService.dto.Actor;
import com.lintang.netflik.movieQueryService.dto.AddMovieReq;
import com.lintang.netflik.movieQueryService.dto.Creator;
import com.lintang.netflik.movieQueryService.entity.ActorEntity;
import com.lintang.netflik.movieQueryService.entity.CreatorEntity;
import com.lintang.netflik.movieQueryService.entity.MovieEntity;
import com.lintang.netflik.movieQueryService.event.AddMovieEvent;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.stream.Collectors;

@Component
public class MovieEntityMapper {


    public MovieEntity toEntity(AddMovieReq model) {
        MovieEntity entity = new MovieEntity();
        Timestamp modelTimestamp = Timestamp.valueOf(model.getrYear().atStartOfDay());
        MovieEntity newEntity= entity.setName(model.getName()).setType(model.getType())
                .setSynopsis(model.getSynopsis()).setMpaRating(model.getMpaRating())
                .setrYear(modelTimestamp).setIdmbRating(model.getIdmbRating())
                .setImage(model.getImage());
        return newEntity;
    }


    public MovieEntity addMovieEventToEntity(AddMovieEvent movieEvent) {
        MovieEntity entity = new MovieEntity();
        Timestamp modelTimestamp = Timestamp.valueOf(movieEvent.getrYear().atStartOfDay());

        MovieEntity newEntity = entity.setId(movieEvent.getId())
                .setName(movieEvent.getName())
                .setType(movieEvent.getType())
                .setSynopsis(movieEvent.getSynopsis())
                .setMpaRating(movieEvent.getMpaRating())
                .setrYear(modelTimestamp)
                .setIdmbRating(movieEvent.getIdmbRating())
                .setImage(movieEvent.getImage()).setActors(movieEvent.getActors().stream().map(actor -> toActorEntity(actor)).collect(Collectors.toSet()))
                .setCreators(movieEvent.getCreators().stream().map(creator -> toCreatorEntity(creator)).collect(Collectors.toSet()))
                ;
        return newEntity;
    }

    public MovieEntity updateMovietoEntity(AddMovieEvent movieEvent, MovieEntity movie) {
        Timestamp modelTimestamp = Timestamp.valueOf(movieEvent.getrYear().atStartOfDay());

        MovieEntity newEntity = movie.setId(movie.getId())
                .setName(movieEvent.getName())
                .setType(movieEvent.getType())
                .setSynopsis(movieEvent.getSynopsis())
                .setMpaRating(movieEvent.getMpaRating())
                .setrYear(modelTimestamp)
                .setIdmbRating(movieEvent.getIdmbRating())
                .setImage(movieEvent.getImage()).setActors(movieEvent.getActors().stream().map(actor -> toActorEntity(actor)).collect(Collectors.toSet()))
                .setCreators(movieEvent.getCreators().stream().map(creator -> toCreatorEntity(creator)).collect(Collectors.toSet()))
                ;
        return newEntity;
    }
    public ActorEntity toActorEntity(Actor actor) {
        ActorEntity actorEntity = new ActorEntity();
        ActorEntity entity = actorEntity.setId(actor.getId()).setName(actor.getName());
        return entity;
    }
    public CreatorEntity toCreatorEntity(Creator creator) {
        CreatorEntity creatorEntity = new CreatorEntity();
        CreatorEntity entity = creatorEntity.setId(creator.getId()).setName(creator.getName());
        return entity;
    }



}
