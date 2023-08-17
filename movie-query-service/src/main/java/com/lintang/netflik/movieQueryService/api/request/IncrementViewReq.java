package com.lintang.netflik.movieQueryService.api.request;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class IncrementViewReq {
    private String userId;

//    in seconds
    private int newOffset;
}
