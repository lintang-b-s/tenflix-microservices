package com.lintang.netflik.movieQueryService.api.respone;


import com.lintang.netflik.movieQueryService.broker.message.Video;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetVideoResponse {
    int videoOffset;
    Video video;
}
