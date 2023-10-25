package com.kafkastreams.movieservice.api.response;

import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Movie {
    private int id;

    private String name;

    private String type;

    private String synopsis;

    private String mpaRating;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate rYear;

    private Integer idmbRating;

    @Valid
    private Set<Actor> actors;

    @Valid
    private Set<Creator> creators;

    @Valid
    private Set<Video> videos;

    private String image;

    public LocalDate getrYear() {
        return rYear;
    }

    private Set<Tag> tags;
    private Set<Category> categories;

}
