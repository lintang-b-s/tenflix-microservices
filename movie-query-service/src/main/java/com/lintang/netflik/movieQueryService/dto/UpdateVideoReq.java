package com.lintang.netflik.movieQueryService.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateVideoReq {

    private int id;

    private String url;


    private Integer length;


    private String title;


    private String synopsis;

    private String movieId;

    public UpdateVideoReq setUrl(String url) {
        this.url = url;
        return this;
    }
}
