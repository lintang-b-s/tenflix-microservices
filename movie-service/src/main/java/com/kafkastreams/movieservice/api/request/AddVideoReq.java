package com.kafkastreams.movieservice.api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddVideoReq {


    private String url;
    private String publicId;


    private Integer length;


    private String title;


    private String synopsis;

    private int movieId;

    public AddVideoReq setUrl(String url) {
        this.url = url;
        return this;
    }
}
