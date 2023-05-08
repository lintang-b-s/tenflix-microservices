package com.lintang.netflik.movieQueryService.service;

import com.lintang.netflik.movieQueryService.dto.AddVideoReq;
import com.lintang.netflik.movieQueryService.dto.UpdateVideoReq;
import com.lintang.netflik.movieQueryService.entity.MovieEntity;
import com.lintang.netflik.movieQueryService.entity.VideoEntity;
import com.lintang.netflik.movieQueryService.helper.entityMapper.VideoEntityMapper;
import com.lintang.netflik.movieQueryService.repository.MovieRepository;
import com.lintang.netflik.movieQueryService.repository.VideoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Slf4j
@Service
public class VideoService {
    private VideoRepository repository;
    private VideoEntityMapper mapper;
    private MovieRepository movieRepository;
    public VideoService(VideoRepository repository, VideoEntityMapper mapper,
                        MovieRepository movieRepository) {this.repository = repository;
    this.mapper = mapper;
    this.movieRepository = movieRepository;
    }

    public Iterable<VideoEntity> getVideosByMovieId(@NotNull @Valid int movieId) {
        return repository.findByMovieId(movieId);
    }

    public VideoEntity getVideoByMovieIdAndId(@NotNull @Valid int movieId,@NotNull @Valid int videoId) {
        return repository.getVideoEntitiesByMovie_IdAndId(movieId, videoId).get();
    }


}
