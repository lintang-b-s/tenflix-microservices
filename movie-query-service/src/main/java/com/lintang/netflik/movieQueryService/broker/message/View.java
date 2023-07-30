package com.lintang.netflik.movieQueryService.broker.message;


import com.lintang.netflik.movieQueryService.entity.Platform;
import com.lintang.netflik.movieQueryService.entity.VideoEntity;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class View {
    private int id;

    private String userId;


    private int offset;

    private Platform platform;

    private LocalDateTime createdTimeStamp;
}
