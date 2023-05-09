package com.lintang.netflik.movieQueryService.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;


@NoArgsConstructor
//@AllArgsConstructor
public class ActorEntity {

    @Id
    private int id;

    @NotNull(message = "Actor name is required")
    private String name;

    public ActorEntity( String name) {

        this.name = name;
    }

    @JsonIgnore
    @DBRef
    private Set<MovieEntity> movies = new HashSet<>();



    public int getId() {
        return id;
    }

    public ActorEntity setId(int id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public ActorEntity setName(String name) {
        this.name = name;
        return this;
    }

    public Set<MovieEntity> getMovies() {
        return movies;
    }

    public ActorEntity setMovies(Set<MovieEntity> movies) {
        this.movies = movies;
        return this;
    }

    public void addMovie(MovieEntity movie) {
        this.movies.add(movie);
    }

    public void removeMovie(MovieEntity movie) {
        this.movies.remove(movie);
    }


    @Override
    public String toString() {
        return "ActorEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", movies=" + movies +
                '}';
    }
}
