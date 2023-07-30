package com.lintang.netflik.movieQueryService.broker.message;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Tag {
    private int id;
    private String name;
}
