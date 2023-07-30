package com.lintang.netflik.movieQueryService.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.HashSet;
import java.util.Set;


@Document("videos")
public class VideoEntity {
    @Id
    private int id;

    private String url;
    private String publicId;

    private int length;

    private String title;

    private String synopsis;

    @DocumentReference(lazy = true)
    private MovieEntity movie;

//     view dari useer setiap getvideobyId
    @DocumentReference
    @JsonIgnore
    private Set<ViewEntity> views = new HashSet<>();


    public int getId() {
        return id;
    }

    public VideoEntity setId(int id) {
        this.id = id;
        return this;
    }


    public String getUrl() {
        return url;
    }

    public VideoEntity setUrl(String url) {
        this.url = url;
        return this;
    }

    public int getLength() {
        return length;
    }

    public VideoEntity setLength(int length) {
        this.length = length;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public VideoEntity setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public VideoEntity setSynopsis(String synopsis) {
        this.synopsis = synopsis;
        return this;
    }


    public MovieEntity getMovie() {
        return movie;
    }

    public VideoEntity setMovie(MovieEntity movie) {
        this.movie = movie;
        return this;
    }

    public void addMovie(MovieEntity movie) {
        this.movie = movie;
    }

    public void removeMovie(MovieEntity movie) {
        this.movie = null;
    }


    public Set<ViewEntity> getViews() {
        return views;
    }

    public VideoEntity setViews(Set<ViewEntity> views) {
        this.views = views;
        return this;
    }

    public String getPublicId() {
        return publicId;
    }

    public VideoEntity setPublicId(String publicId) {
        this.publicId = publicId;
        return this;
    }

    @Override
    public String toString() {
        return "VideoEntity{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", publicId='" + publicId + '\'' +
                ", length=" + length +
                ", title='" + title + '\'' +
                ", synopsis='" + synopsis + '\'' +
                ", movie=" + movie +
                ", views=" + views +
                '}';
    }
}
