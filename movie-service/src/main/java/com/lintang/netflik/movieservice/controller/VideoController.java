package com.lintang.netflik.movieservice.controller;


import com.lintang.netflik.movieservice.dto.AddVideoReq;
import com.lintang.netflik.movieservice.dto.UpdateVideoReq;
import com.lintang.netflik.movieservice.dto.Video;
import com.lintang.netflik.movieservice.helper.DtoMapper.VideoDtoMapper;
import com.lintang.netflik.movieservice.service.VideoService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/v1/movie-service/videos")
@AllArgsConstructor
@PreAuthorize("hasAuthority('ROLE_admin')")
public class VideoController {
    private VideoDtoMapper mapper;
    private VideoService videoService;


    @PostMapping
    public ResponseEntity<Video> save(@RequestBody AddVideoReq newVideo) {
        return ok(mapper.videoEntitySavetoVideoDto(videoService.save(newVideo)));
    }

    @PostMapping("/add")
    public ResponseEntity<Video> addVideoByMovie(@RequestBody AddVideoReq newVideo) {
        return ok(mapper.videoEntityToVideoDto(videoService.addVideoByMovieId(newVideo)));

    }



    @DeleteMapping("/{movieId}/{videoId}")
    public ResponseEntity<String> deleteVideoFromMovie(@PathVariable(value = "movieId") int movieId,
                                                      @PathVariable(value = "videoId") int videoId){
        return ok(videoService.deleteVideoFromMovie(movieId,videoId));
    }

    @PutMapping("/{movieId}/{videoId}")
    public ResponseEntity<Video> updateVideoFromMovie(@PathVariable(value = "movieId") int movieId,
                                                      @PathVariable(value = "videoId") int videoId,
                                                      UpdateVideoReq newVideo) {
        return ok(mapper.videoEntityToVideoDto(videoService.updateVideoFromMovie(movieId, videoId, newVideo)));
    }

}
