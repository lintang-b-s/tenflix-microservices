package com.lintang.netflik.movieservice.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "categories")
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @ManyToMany(mappedBy = "categories",  fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<MovieEntity> movies = new HashSet<>();


    public int getId() {
        return id;
    }

    public CategoryEntity setId(int id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public CategoryEntity setName(String name) {
        this.name = name;
        return this;
    }

    public Set<MovieEntity> getMovies() {
        return movies;
    }

    public CategoryEntity setMovies(Set<MovieEntity> movies) {
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
        return "CategoryEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", movies=" + movies +
                '}';
    }
}
