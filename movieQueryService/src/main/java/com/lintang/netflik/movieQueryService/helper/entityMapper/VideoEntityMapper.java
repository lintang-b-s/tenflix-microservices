package com.lintang.netflik.movieQueryService.helper.entityMapper;

import com.lintang.netflik.movieQueryService.dto.AddVideoReq;
import com.lintang.netflik.movieQueryService.dto.Video;
import com.lintang.netflik.movieQueryService.entity.MovieEntity;
import com.lintang.netflik.movieQueryService.entity.VideoEntity;
import org.springframework.stereotype.Component;

@Component
public class VideoEntityMapper {
    public VideoEntity saveEntity(AddVideoReq videoDto) {
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
        movie.setId(video.getMovieId());
        return entity.setId(video.getId());

    }
}
