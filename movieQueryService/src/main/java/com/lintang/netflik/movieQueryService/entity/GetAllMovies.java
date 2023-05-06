package com.lintang.netflik.movieQueryService.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;


@Entity
public class GetAllMovies {
    @Id
    private int id;
    private String name;

    private String type;

    private String synopsis;

    private String mpaRating;

    private Timestamp rYear;

    private int idmbRating;
    private String image;

//    private int actor_id;
//
//    private String actor_name;
//    private int creator_id;
//
//    private String creator_name;
//
//    private int video_id;
//
//    private String url;
//
//    private int length;
//
//    private String title;
//
//    private String video_synopsis;

    @ManyToMany( fetch = FetchType.LAZY)
    @JoinTable(
            name="movie_actor",
            joinColumns = @JoinColumn(name = "movie_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "actor_id", referencedColumnName = "id")
    )
    @JsonIgnore
    private Set<ActorEntity> actors= new HashSet<>();


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name="movie_creator",
            joinColumns = @JoinColumn(name = "movie_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "creator_id", referencedColumnName = "id")
    )
    @JsonIgnore
    private Set<CreatorEntity> creators= new HashSet<>() ;


    @OneToMany(mappedBy = "movie",  fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<VideoEntity> videos = new HashSet<>();


    public int getId() {
        return id;
    }

    public GetAllMovies setId(int id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public GetAllMovies setName(String name) {
        this.name = name;
        return this;
    }

    public String getType() {
        return type;
    }

    public GetAllMovies setType(String type) {
        this.type = type;
        return this;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public GetAllMovies setSynopsis(String synopsis) {
        this.synopsis = synopsis;
        return this;
    }

    public String getMpaRating() {
        return mpaRating;
    }

    public GetAllMovies setMpaRating(String mpaRating) {
        this.mpaRating = mpaRating;
        return this;
    }

    public Timestamp getrYear() {
        return rYear;
    }

    public GetAllMovies setrYear(Timestamp rYear) {
        this.rYear = rYear;
        return this;
    }

    public int getIdmbRating() {
        return idmbRating;
    }

    public GetAllMovies setIdmbRating(int idmbRating) {
        this.idmbRating = idmbRating;
        return this;
    }

    public String getImage() {
        return image;
    }

    public GetAllMovies setImage(String image) {
        this.image = image;
        return this;
    }

    public Set<ActorEntity> getActors() {
        return actors;
    }

    public GetAllMovies setActors(Set<ActorEntity> actors) {
        this.actors = actors;
        return this;
    }

    public Set<CreatorEntity> getCreators() {
        return creators;
    }

    public GetAllMovies setCreators(Set<CreatorEntity> creators) {
        this.creators = creators;
        return this;
    }

    public Set<VideoEntity> getVideos() {
        return videos;
    }

    public GetAllMovies setVideos(Set<VideoEntity> videos) {
        this.videos = videos;
        return this;
    }
}
