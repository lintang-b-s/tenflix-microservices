package com.lintang.netflik.movieservice.broker.message;

import com.lintang.netflik.movieservice.api.response.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AddMovieMessage {

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




}
