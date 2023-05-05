package com.lintang.netflik.movieQueryService.controller;


import com.lintang.netflik.movieQueryService.dto.AddVideoReq;
import com.lintang.netflik.movieQueryService.dto.UpdateVideoReq;
import com.lintang.netflik.movieQueryService.dto.Video;
import com.lintang.netflik.movieQueryService.helper.DtoMapper.VideoDtoMapper;
import com.lintang.netflik.movieQueryService.service.VideoService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/v1/videos")
@AllArgsConstructor
public class VideoController {
    private VideoDtoMapper mapper;
    private VideoService videoService;


    @GetMapping("/{movieId}")
    public ResponseEntity<List<Video>> getVideosByMovieId(@PathVariable(value = "movieId") int movieId) {
        return ok(mapper.toListModel(videoService.getVideosByMovieId(movieId)));
    }

    @GetMapping("/{movieId}/{videoId}")
    public ResponseEntity<Video> getVideoByMovieIdAndId(@PathVariable(value = "movieId") int movieId,
                                                        @PathVariable(value = "videoId") int videoId){
        return ok(mapper.videoEntityToVideoDto(videoService.getVideoByMovieIdAndId(movieId, videoId)));
    }

}
