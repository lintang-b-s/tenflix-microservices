package com.lintang.netflik.movieQueryService.broker.message;

import com.lintang.netflik.movieQueryService.entity.ViewEntity;
import lombok.*;

import java.util.HashSet;
import java.util.Set;


@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Video {

    private int id;


    private String url;
    private String publicId;


    private Integer length;


    private String title;


    private String synopsis;

    private int movieId;
//    private Set<View> views ;

    public Video(int id, String url, Integer length, String title, String synopsis) {
        this.id = id;
        this.url = url;
        this.length = length;
        this.title = title;
        this.synopsis = synopsis;
    }
}
