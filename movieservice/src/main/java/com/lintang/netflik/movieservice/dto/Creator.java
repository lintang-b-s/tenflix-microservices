package com.lintang.netflik.movieservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Creator {

    private int id;


    private String name;
}
