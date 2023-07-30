package com.lintang.netflik.movieservice.util.messageMapper;


import com.lintang.netflik.movieservice.broker.message.AddVideoMessage;
import com.lintang.netflik.movieservice.entity.VideoEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class VideoMessageMapper {



    public AddVideoMessage videoEntityToMessage(VideoEntity v) {
        AddVideoMessage videoMessage = AddVideoMessage.builder()
                .id(v.getId()).url(v.getUrl())
                .length(v.getLength()).title(v.getTitle())
                .synopsis(v.getSynopsis()).movieId(String.valueOf(v.getMovie().getId()))
                .publicId(v.getPublicId())
                .build();
        return videoMessage;
    }
}
