package com.lintang.netflik.movieQueryService.util.entityMapper;

import com.lintang.netflik.movieQueryService.broker.message.*;
import com.lintang.netflik.movieQueryService.dto.AddMovieReq;
import com.lintang.netflik.movieQueryService.entity.*;
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


    public MovieEntity addMovieEventToEntity(AddMovieMessage movieEvent) {
        MovieEntity entity = new MovieEntity();
        Timestamp modelTimestamp = Timestamp.valueOf(movieEvent.getRYear());

        MovieEntity newEntity = entity.setId(movieEvent.getId())
                .setName(movieEvent.getName())
                .setType(movieEvent.getType())
                .setSynopsis(movieEvent.getSynopsis())
                .setMpaRating(movieEvent.getMpaRating())
                .setrYear(modelTimestamp)
                .setIdmbRating(movieEvent.getIdmbRating())
                .setImage(movieEvent.getImage()).setActors(movieEvent.getActors().stream().map(actor -> toActorEntity(actor)).collect(Collectors.toSet()))
                .setCreators(movieEvent.getCreators().stream().map(creator -> toCreatorEntity(creator)).collect(Collectors.toSet()))
                .setTags(movieEvent.getTags().stream().map(tag -> toTagEntity(tag)).collect(Collectors.toSet()))
                .setCategories(movieEvent.getCategories().stream().map(cat -> toCategory(cat)).collect(Collectors.toSet()))
                ;
        return newEntity;
    }

    public MovieEntity updateMovietoEntity(AddMovieMessage movieEvent, MovieEntity movie) {
        Timestamp modelTimestamp = Timestamp.valueOf(movieEvent.getRYear());

        MovieEntity newEntity = movie.setId(movie.getId())
                .setName(movieEvent.getName())
                .setType(movieEvent.getType())
                .setSynopsis(movieEvent.getSynopsis())
                .setMpaRating(movieEvent.getMpaRating())
                .setrYear(modelTimestamp)
                .setIdmbRating(movieEvent.getIdmbRating())
                .setImage(movieEvent.getImage()).setActors(movieEvent.getActors().stream().map(actor -> toActorEntity(actor)).collect(Collectors.toSet()))
                .setCreators(movieEvent.getCreators().stream().map(creator -> toCreatorEntity(creator)).collect(Collectors.toSet()))
                .setTags(movieEvent.getTags().stream().map(tag -> toTagEntity(tag)).collect(Collectors.toSet()))
                .setCategories(movieEvent.getCategories().stream().map(cat -> toCategory(cat)).collect(Collectors.toSet()))
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

    public TagEntity toTagEntity(Tag tag) {
        TagEntity tagEn = TagEntity.builder()
                .id(tag.getId()).name(tag.getName())
                .build();

        return tagEn;
    }

    public CategoryEntity toCategory(Category cat) {
        CategoryEntity catEn = CategoryEntity.builder()
                .id(cat.getId()).name(cat.getName())
                .build();
        return catEn;
    }

}
