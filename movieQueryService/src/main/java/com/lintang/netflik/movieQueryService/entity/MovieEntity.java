package com.lintang.netflik.movieQueryService.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@Document("movies")
public class MovieEntity {
    @Id
    private int id;

    private String name;

    private String type;

    private String synopsis;

    private String mpaRating;

    private Date rYear;

    private int idmbRating;
    private String image;


    @JsonIgnore
    private Set<ActorEntity> actors= new HashSet<>();



    @JsonIgnore
    private Set<CreatorEntity> creators= new HashSet<>() ;


    @DocumentReference
    @JsonIgnore
    private Set<VideoEntity> videos = new HashSet<>();


    public int getId() {
        return id;
    }

    public MovieEntity setId(int id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public MovieEntity setName(String name) {
        this.name = name;
        return this;
    }

    public String getType() {
        return type;
    }

    public MovieEntity setType(String type) {
        this.type = type;
        return this;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public MovieEntity setSynopsis(String synopsis) {
        this.synopsis = synopsis;
        return this;
    }

    public String getMpaRating() {
        return mpaRating;
    }

    public MovieEntity setMpaRating(String mpaRating) {
        this.mpaRating = mpaRating;
        return this;
    }

    public Date getrYear() {
        return rYear;
    }

    public MovieEntity setrYear(Timestamp rYear) {
        this.rYear = rYear;
        return this;
    }

    public int getIdmbRating() {
        return idmbRating;
    }

    public MovieEntity setIdmbRating(int idmbRating) {
        this.idmbRating = idmbRating;
        return this;
    }



    public MovieEntity addActor(ActorEntity actor) {
        this.actors.add(actor);
        actor.getMovies().add(this);
        return this;
    }

    public MovieEntity addCreator(CreatorEntity creator) {
        this.creators.add(creator);
        creator.getMovies().add(this);
        return this;
    }

    public Set<ActorEntity> getActors() {
        return actors;
    }

    public MovieEntity setActors(Set<ActorEntity> actors) {
        this.actors = actors;
        return this;
    }

    public Set<CreatorEntity> getCreators() {
        return creators;
    }

    public MovieEntity setCreators(Set<CreatorEntity> creators) {
        this.creators = creators;
        return this;
    }


    public Set<VideoEntity> getVideos() {
        return videos;
    }

    public MovieEntity setVideos(Set<VideoEntity> videos) {
        this.videos = videos;
        return this;
    }

    public String getImage() {
        return image;
    }

    public MovieEntity setImage(String image) {
        this.image = image;
        return this;
    }

    @Override
    public String toString() {
        return "MovieEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", synopsis='" + synopsis + '\'' +
                ", mpaRating='" + mpaRating + '\'' +
                ", rYear=" + rYear +
                ", idmbRating=" + idmbRating +
                ", image='" + image + '\'' +
                ", actors=" + actors +
                ", creators=" + creators +
                ", videos=" + videos +
                '}';
    }


}
