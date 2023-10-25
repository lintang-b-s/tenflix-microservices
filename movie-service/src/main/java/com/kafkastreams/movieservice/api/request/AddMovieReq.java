package com.kafkastreams.movieservice.api.request;

import javax.validation.Valid;

import com.kafkastreams.movieservice.api.response.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@Data
@AllArgsConstructor
@ToString
public class AddMovieReq {

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

    private Set<Video> videos;

    @Valid
    private Set<Tag> tags;

    @Valid
    private Set<Category> categories;

    private String image;

    private Boolean notification;

    public LocalDate getrYear() {
        return rYear;
    }
}
