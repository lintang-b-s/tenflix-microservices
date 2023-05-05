package com.lintang.netflik.movieQueryService.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
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
    private Set<Actor> actors ;


    @Valid
    private Set<Creator> creators ;

    private Set<Video> videos;





    private String image ;

    public LocalDate getrYear() {
        return rYear;
    }
}
