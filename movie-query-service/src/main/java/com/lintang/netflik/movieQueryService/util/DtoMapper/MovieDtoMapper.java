package com.lintang.netflik.movieQueryService.util.DtoMapper;

import com.lintang.netflik.movieQueryService.broker.message.*;
import com.lintang.netflik.movieQueryService.entity.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

@Component
public class MovieDtoMapper {
    public Movie movieEntitytoMovieDto(MovieEntity entity) {
        Movie movie = new Movie();
        LocalDate entityLDT = entity.getrYear().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        movie.setName(entity.getName());movie.setImage(entity.getImage());
        movie.setId(entity.getId());movie.setType(entity.getType());
        movie.setSynopsis(entity.getSynopsis());movie.setMpaRating(entity.getMpaRating());
        movie.setRYear(entityLDT );
        movie.setIdmbRating(entity.getIdmbRating());movie.setCreators(entity.getCreators().stream().map(m -> toCreatorModel(m)).collect(Collectors.toSet()));

        movie.setActors(entity.getActors().stream().map(m -> toActorModel(m)).collect(Collectors.toSet()));
        movie.setVideos(entity.getVideos().stream().map(m -> toVideoModel(m)).collect(Collectors.toSet()));
        movie.setTags(entity.getTags().stream().map(m -> toTagModel(m)).collect(Collectors.toSet()));
        movie.setCategories(entity.getCategories().stream().map(m -> toCategoryModel(m)).collect(Collectors.toSet()));
        return movie;
    }

    private Category toCategoryModel(CategoryEntity m) {
        Category cat = Category.builder().id(m.getId()).name(m.getName())
                .build();
        return cat;
    }

    private Tag toTagModel(TagEntity m) {
        Tag tag = Tag.builder().id(m.getId()).name(m.getName())
                .build();
        return tag;
    }

    public List<Movie> toListModel(Iterable<MovieEntity> entities) {
        if (Objects.isNull(entities)) {
            return Collections.emptyList();
        }
        return StreamSupport.stream(entities.spliterator(), false).map(e -> movieEntitytoMovieDto(e))
                .collect(toList());
    }



    public  Actor getAllMovieEntityToActorModel(int id, String name) {
        Actor actor = new Actor();
         actor.setId(id);actor.setName(name);
         return actor;
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
