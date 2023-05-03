package com.lintang.netflik.movieservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Video {

    private int id;


    private String url;


    private Integer length;


    private String title;


    private String synopsis;

    private int movieId;

    public Video(int id, String url, Integer length, String title, String synopsis) {
        this.id = id;
        this.url = url;
        this.length = length;
        this.title = title;
        this.synopsis = synopsis;
    }
}
