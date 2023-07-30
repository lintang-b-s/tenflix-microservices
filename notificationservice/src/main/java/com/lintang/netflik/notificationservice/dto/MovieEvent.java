package com.lintang.netflik.notificationservice.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Set;


@NoArgsConstructor
@AllArgsConstructor
public class MovieEvent {

    private int id;
    private String name;
    private String type;
    private String synopsis;
    private String mpaRating;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDateTime rYear;
    private int idmbRating;
    private Set<Actor> actors ;

    private Set<Creator> creators;
    private Set<Video> videos ;


    private String image ;

    private Set<Tag> tags;
    private Set<Category> categories;

    private ZonedDateTime createdAt;

    public MovieEvent setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }



    public LocalDateTime getrYear() {
        return rYear;
    }

    public int getId() {
        return id;
    }

    public MovieEvent setId(int id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public MovieEvent setName(String name) {
        this.name = name;
        return this;
    }

    public String getType() {
        return type;
    }

    public MovieEvent setType(String type) {
        this.type = type;
        return this;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public MovieEvent setSynopsis(String synopsis) {
        this.synopsis = synopsis;
        return this;
    }

    public String getMpaRating() {
        return mpaRating;
    }

    public MovieEvent setMpaRating(String mpaRating) {
        this.mpaRating = mpaRating;
        return this;
    }

    public MovieEvent setrYear(LocalDateTime rYear) {
        this.rYear = rYear;
        return this;
    }

    public Integer getIdmbRating() {
        return idmbRating;
    }

    public MovieEvent setIdmbRating(Integer idmbRating) {
        this.idmbRating = idmbRating;
        return this;
    }

    public Set<Actor> getActors() {
        return actors;
    }

    public MovieEvent setActors(Set<Actor> actors) {
        this.actors = actors;
        return this;
    }

    public Set<Creator> getCreators() {
        return creators;
    }

    public MovieEvent setCreators(Set<Creator> creators) {
        this.creators = creators;
        return this;
    }

    public Set<Video> getVideos() {
        return videos;
    }

    public MovieEvent setVideos(Set<Video> videos) {
        this.videos = videos;
        return this;
    }

    public String getImage() {
        return image;
    }

    public MovieEvent setImage(String image) {
        this.image = image;
        return this;
    }

    public MovieEvent setIdmbRating(int idmbRating) {
        this.idmbRating = idmbRating;
        return this;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public MovieEvent setTags(Set<Tag> tags) {
        this.tags = tags;
        return this;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public MovieEvent setCategories(Set<Category> categories) {
        this.categories = categories;
        return this;
    }
}

