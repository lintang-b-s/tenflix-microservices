package com.lintang.netflik.movieQueryService.broker.message;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@ToString
public class Category {

    private int id;
    private String name;
}
