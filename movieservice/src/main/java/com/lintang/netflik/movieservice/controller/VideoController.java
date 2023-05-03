package com.lintang.netflik.movieservice.controller;


import com.lintang.netflik.movieservice.dto.AddVideoReq;
import com.lintang.netflik.movieservice.dto.UpdateVideoReq;
import com.lintang.netflik.movieservice.dto.Video;
import com.lintang.netflik.movieservice.helper.DtoMapper.VideoDtoMapper;
import com.lintang.netflik.movieservice.service.VideoService;
import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
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

    @PostMapping("/tes")
    public String tes() {return "tes";}

    @PostMapping
    public ResponseEntity<Video> save(@RequestBody AddVideoReq newVideo) {
        return ok(mapper.videoEntitySavetoVideoDto(videoService.save(newVideo)));
    }

    @PostMapping("/add")
    public ResponseEntity<Video> addVideoByMovie(@RequestBody AddVideoReq newVideo) {
        return ok(mapper.videoEntityToVideoDto(videoService.addVideoByMovieId(newVideo)));

    }

    @GetMapping("/{movieId}")
    public ResponseEntity<List<Video>> getVideosByMovieId(@PathVariable(value = "movieId") int movieId) {
        return ok(mapper.toListModel(videoService.getVideosByMovieId(movieId)));
    }

    @GetMapping("/{movieId}/{videoId}")
    public ResponseEntity<Video> getVideoByMovieIdAndId(@PathVariable(value = "movieId") int movieId,
                                                        @PathVariable(value = "videoId") int videoId){
        return ok(mapper.videoEntityToVideoDto(videoService.getVideoByMovieIdAndId(movieId, videoId)));
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
