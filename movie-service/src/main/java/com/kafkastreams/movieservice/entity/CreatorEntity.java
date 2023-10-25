package com.kafkastreams.movieservice.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "creators")
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class CreatorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull(message = "Creator name is required")
    private String name;

    @CreationTimestamp
    private Instant createdOn;
    @UpdateTimestamp
    private Instant lastUpdatedOn;

    public CreatorEntity(String name) {

        this.name = name;
    }

    public CreatorEntity(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // @JsonIgnore
    @ManyToMany(mappedBy = "creators", fetch = FetchType.LAZY)
    private Set<MovieEntity> movies = new HashSet<MovieEntity>();

    public int getId() {
        return id;
    }

    public CreatorEntity setId(int id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public CreatorEntity setName(String name) {
        this.name = name;
        return this;
    }

    public Set<MovieEntity> getMovies() {
        return movies;
    }

    public CreatorEntity setMovies(Set<MovieEntity> movies) {
        this.movies = movies;
        return this;
    }

    // public void addMovie(MovieEntity movie) {
    // this.movies.add(movie);
    // }
    //
    public void removeMovie(MovieEntity movie) {
        this.movies.remove(movie);
    }
    //

}
