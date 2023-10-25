package com.kafkastreams.movieservice.util.DtoMapper;

import com.kafkastreams.movieservice.api.response.*;
import com.kafkastreams.movieservice.entity.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
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
        LocalDate entityLDT = entity.getrYear().toLocalDateTime().toLocalDate();

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

    public List<Movie> toListModel(Iterable<MovieEntity> entities) {
        if (Objects.isNull(entities)) {
            return Collections.emptyList();
        }
        return StreamSupport.stream(entities.spliterator(), false).map(e -> movieEntitytoMovieDto(e))
                .collect(toList());
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

    private Tag toTagModel(TagEntity tag) {
        Tag m = Tag.builder().id(tag.getId()).name(tag.getName())
                .build();
        return m;
    }

    private Category toCategoryModel(CategoryEntity c) {
        Category m = Category.builder().id(c.getId()).name(c.getName())
                .build();
        return m;
    }
}
