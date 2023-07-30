package com.lintang.netflik.movieQueryService.util.DtoMapper;

import com.lintang.netflik.movieQueryService.broker.message.Video;
import com.lintang.netflik.movieQueryService.broker.message.View;
import com.lintang.netflik.movieQueryService.entity.VideoEntity;
import com.lintang.netflik.movieQueryService.entity.ViewEntity;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

@Component
public class VideoDtoMapper {
    public Video videoEntityToVideoDto(VideoEntity video) {
        Video videoDto = new Video(video.getId(), video.getUrl(), video.getPublicId(),
                video.getLength(), video.getTitle(), video.getSynopsis(), video.getMovie().getId());
        return videoDto;
    }

    public Video videoEntitySavetoVideoDto(VideoEntity video) {
        Video videoDto = new Video(video.getId(), video.getUrl(),
                video.getLength(), video.getTitle(), video.getSynopsis());
        return videoDto;
    }

//    public View viewEntityToDto(Set<ViewEntity> view) {
//        View v = View.builder().id(view.getId()).userId(view.getUserId()).offset(view.getOffset())
//                .platform(view.getPlatform()).createdTimeStamp(view.getCreatedTimeStamp())
//                .build();
//    }
    public List<Video> toListModel(Iterable<VideoEntity> entities) {
        if (Objects.isNull(entities)) {
            return Collections.emptyList();
        }
        return StreamSupport.stream(entities.spliterator(), false).map(e -> videoEntityToVideoDto(e))
                .collect(toList());
    }
}
