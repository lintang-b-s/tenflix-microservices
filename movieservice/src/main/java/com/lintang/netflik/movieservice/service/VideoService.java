package com.lintang.netflik.movieservice.service;

import com.lintang.netflik.movieservice.dto.AddVideoReq;
import com.lintang.netflik.movieservice.dto.UpdateVideoReq;
import com.lintang.netflik.movieservice.entity.MovieEntity;
import com.lintang.netflik.movieservice.entity.VideoEntity;
import com.lintang.netflik.movieservice.helper.entityMapper.VideoEntityMapper;
import com.lintang.netflik.movieservice.repository.MovieRepository;
import com.lintang.netflik.movieservice.repository.VideoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Optional;

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


    public VideoEntity save( @Valid AddVideoReq newVideo) {
        return repository.save(mapper.saveEntity(newVideo));
    }


    public Iterable<VideoEntity> getVideosByMovieId(@NotNull @Valid int movieId) {
        return repository.findVideoEntitiesByMovieEntityId(movieId);
    }


    public VideoEntity addVideoByMovieId( @Valid AddVideoReq newVideo) {
        MovieEntity movie = movieRepository.findById(newVideo.getMovieId()).get();
        return repository.save(mapper.toEntity(newVideo, movie));
    }


    public VideoEntity getVideoByMovieIdAndId(@NotNull @Valid int movieId,@NotNull @Valid int videoId) {
        return repository.getVideoEntitiesByMovie_IdAndId(movieId, videoId).get();
    }



    public String deleteVideoFromMovie(@NotNull @Valid int movieId, @NotNull @Valid int videoId) {
       repository.deleteVideoEntitiesByMovie_IdAndId(movieId, videoId);
        return "movie deleted!";
    }


    public VideoEntity updateVideoFromMovie(@NotNull @Valid int videoId,
                                                      @NotNull @Valid int movieId,
                                                      @Valid UpdateVideoReq newVideo){

        MovieEntity movie = movieRepository.findById(movieId).get();
        VideoEntity videoFromDb = repository.findById(videoId).get();
        videoFromDb.setUrl(newVideo.getUrl()).setLength(newVideo.getLength())
                .setTitle(newVideo.getTitle()).setSynopsis(newVideo.getSynopsis());


        return repository.save(videoFromDb);
    }




}
