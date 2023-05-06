package com.lintang.netflik.movieservice.event;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lintang.netflik.movieservice.dto.Actor;
import com.lintang.netflik.movieservice.dto.Creator;
import com.lintang.netflik.movieservice.dto.Video;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Set;



@NoArgsConstructor
@AllArgsConstructor
public class AddMovieEvent {
    private int id;


    private String name;


    private String type;


    private String synopsis;


    private String mpaRating;

    private LocalDate rYear;


    private Integer idmbRating;


    @Valid
    private Set<Actor> actors ;


    @Valid
    private Set<Creator> creators;


    @Valid
    private Set<Video> videos ;


    private String image ;

    private String outboxType;

    public String getOutboxType() {
        return outboxType;
    }

    public AddMovieEvent setOutboxType(String outboxType) {
        this.outboxType = outboxType;
        return this;
    }

    public LocalDate getrYear() {
        return rYear;
    }

    public int getId() {
        return id;
    }

    public AddMovieEvent setId(int id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public AddMovieEvent setName(String name) {
        this.name = name;
        return this;
    }

    public String getType() {
        return type;
    }

    public AddMovieEvent setType(String type) {
        this.type = type;
        return this;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public AddMovieEvent setSynopsis(String synopsis) {
        this.synopsis = synopsis;
        return this;
    }

    public String getMpaRating() {
        return mpaRating;
    }

    public AddMovieEvent setMpaRating(String mpaRating) {
        this.mpaRating = mpaRating;
        return this;
    }

    public AddMovieEvent setrYear(LocalDate rYear) {
        this.rYear = rYear;
        return this;
    }

    public Integer getIdmbRating() {
        return idmbRating;
    }

    public AddMovieEvent setIdmbRating(Integer idmbRating) {
        this.idmbRating = idmbRating;
        return this;
    }

    public Set<Actor> getActors() {
        return actors;
    }

    public AddMovieEvent setActors(Set<Actor> actors) {
        this.actors = actors;
        return this;
    }

    public Set<Creator> getCreators() {
        return creators;
    }

    public AddMovieEvent setCreators(Set<Creator> creators) {
        this.creators = creators;
        return this;
    }

    public Set<Video> getVideos() {
        return videos;
    }

    public AddMovieEvent setVideos(Set<Video> videos) {
        this.videos = videos;
        return this;
    }

    public String getImage() {
        return image;
    }

    public AddMovieEvent setImage(String image) {
        this.image = image;
        return this;
    }
}
