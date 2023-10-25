package com.kafkastreams.movieservice.api.request;

import javax.validation.Valid;
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

    @Valid
    private Integer length;

    private String title;

    private String synopsis;

    public UpdateVideoReq setUrl(String url) {
        this.url = url;
        return this;
    }
}
