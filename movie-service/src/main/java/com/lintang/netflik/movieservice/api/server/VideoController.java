package com.lintang.netflik.movieservice.api.server;


import com.lintang.netflik.movieservice.api.request.AddVideoReq;
import com.lintang.netflik.movieservice.api.request.UpdateVideoReq;
import com.lintang.netflik.movieservice.api.response.Video;
import com.lintang.netflik.movieservice.command.service.VideoCommandService;
import com.lintang.netflik.movieservice.util.DtoMapper.VideoDtoMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/v1/movie-service/videos")
@AllArgsConstructor
@PreAuthorize("hasAuthority('ROLE_default-roles-tenflix')")
public class VideoController {
    private VideoDtoMapper mapper;
    private VideoCommandService videoService;


    @PostMapping
    public ResponseEntity<Video> save(@RequestBody AddVideoReq newVideo) {
        return ok(mapper.videoEntitySavetoVideoDto(videoService.save(newVideo)));
    }

    @PostMapping("/add")
    public ResponseEntity<Video> addVideoByMovie(@RequestBody AddVideoReq newVideo) {
        return ok(mapper.videoEntityToVideoDto(videoService.addVideoByMovieId(newVideo)));

    }

    @PostMapping("/addUpload")
    public ResponseEntity<Video> addVideoAndUpload(@RequestParam("file") MultipartFile file,
            @RequestBody AddVideoReq newVideo) {
        return ok(mapper.videoEntityToVideoDto(videoService.addVideoAndUpload(newVideo, file)));
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
