package com.kafkastreams.movieservice.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import javax.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "videos")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class VideoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String url;
    private String publicId;

    private int length;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String synopsis;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", referencedColumnName = "id")
    private MovieEntity movie;

    @CreationTimestamp
    private Instant createdOn;
    @UpdateTimestamp
    private Instant lastUpdatedOn;

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

    // public void addMovie(MovieEntity movie) {
    // this.movie = movie;
    // }
    //
    public void removeMovie() {
        this.movie = null;
    }

    public String getPublicId() {
        return publicId;
    }

    public VideoEntity setPublicId(String publicId) {
        this.publicId = publicId;
        return this;
    }

}
