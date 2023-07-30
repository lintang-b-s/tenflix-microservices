package com.lintang.netflik.movieQueryService.util.entityMapper;

import com.lintang.netflik.movieQueryService.broker.message.AddVideoMessage;
import com.lintang.netflik.movieQueryService.broker.message.Video;
import com.lintang.netflik.movieQueryService.dto.AddVideoReq;
import com.lintang.netflik.movieQueryService.entity.MovieEntity;
import com.lintang.netflik.movieQueryService.entity.VideoEntity;
import org.springframework.stereotype.Component;

@Component
public class VideoEntityMapper {
    public VideoEntity saveEntity(Video videoDto) {
        VideoEntity entity = new VideoEntity();
        return entity.setUrl(videoDto.getUrl())
                .setLength(videoDto.getLength()) .setTitle(videoDto.getTitle()).setSynopsis(videoDto.getSynopsis());
    }

    public VideoEntity toEntity(AddVideoReq videoDto, MovieEntity movie) {
        VideoEntity entity = new VideoEntity();
        movie.setId(videoDto.getMovieId());
        return entity.setUrl(videoDto.getUrl())
                .setLength(videoDto.getLength()) .setTitle(videoDto.getTitle()).setSynopsis(videoDto.getSynopsis())
                .setMovie(movie);

    }

    public VideoEntity videoDtoToEntity(Video video, MovieEntity movie) {
        VideoEntity entity = new VideoEntity();
        return entity.setId(video.getId()).setSynopsis(video.getSynopsis()).setLength(video.getLength())
                .setTitle(video.getTitle()).setUrl(video.getUrl()).setPublicId(video.getPublicId());
    }


    public VideoEntity saveMessageEntity(AddVideoMessage videoDto) {
        VideoEntity entity = new VideoEntity();
        return entity.setId(videoDto.getId()).setUrl(videoDto.getUrl())
                .setLength(videoDto.getLength()) .setTitle(videoDto.getTitle()).setSynopsis(videoDto.getSynopsis())
                .setPublicId(videoDto.getPublicId());
    }


}
