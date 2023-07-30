package com.lintang.netflik.movieservice.util.entityMapper;

import com.lintang.netflik.movieservice.api.request.AddVideoReq;
import com.lintang.netflik.movieservice.api.response.Video;
import com.lintang.netflik.movieservice.entity.MovieEntity;
import com.lintang.netflik.movieservice.entity.VideoEntity;
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
                .setMovie(movie).setPublicId(videoDto.getPublicId());
    }

    public VideoEntity toEntityBeforeUpload(AddVideoReq videoDto, MovieEntity movie) {
        VideoEntity entity = new VideoEntity();
        movie.setId(videoDto.getMovieId());
        return entity
                .setPublicId(videoDto.getPublicId())
                .setLength(videoDto.getLength()) .setTitle(videoDto.getTitle()).setSynopsis(videoDto.getSynopsis())
                .setMovie(movie);
    }

    public VideoEntity videoDtoToEntity(Video video, MovieEntity movie) {
        VideoEntity entity = new VideoEntity();
        return entity.setId(video.getId()).setSynopsis(video.getSynopsis()).setLength(video.getLength())
                .setTitle(video.getTitle()).setUrl(video.getUrl());
    }
    public VideoEntity videoDtoToEntity(Video video) {
        VideoEntity entity = new VideoEntity();
        return entity.setSynopsis(video.getSynopsis()).setLength(video.getLength())
                .setTitle(video.getTitle()).setUrl(video.getUrl());
    }






}
