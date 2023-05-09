package com.lintang.netflik.movieQueryService.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lintang.netflik.movieQueryService.dto.Actor;
import com.lintang.netflik.movieQueryService.dto.Creator;
import com.lintang.netflik.movieQueryService.dto.Video;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddMovieEventPayload {
    @JsonProperty
    private int id;
    @JsonProperty
    private String name;

    @JsonProperty
    private String type;
    @JsonProperty
    private String synopsis;

    @JsonProperty
    private String mpaRating;

    @JsonProperty
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate rYear;

    @JsonProperty
    private Integer idmbRating;

    @JsonProperty
    @Valid
    private Set<Actor> actors ;

    @JsonProperty
    @Valid
    private Set<Creator> creators;

    @JsonProperty
    @Valid
    private Set<Video> videos ;

    @JsonProperty
    private String image ;

    @JsonProperty
    private String outboxType;


}
