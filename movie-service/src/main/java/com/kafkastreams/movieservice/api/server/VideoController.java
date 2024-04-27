package com.kafkastreams.movieservice.api.server;


import com.kafkastreams.movieservice.api.request.UpdateVideoReq;
import com.kafkastreams.movieservice.command.service.VideoCommandService;
import com.kafkastreams.movieservice.util.DtoMapper.VideoDtoMapper;
import com.kafkastreams.movieservice.api.request.AddVideoReq;
import com.kafkastreams.movieservice.api.response.Video;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/v1/movie-service/videos")
@PreAuthorize("hasAuthority('ROLE_user')")
public class VideoController {
    private VideoDtoMapper mapper;
    private VideoCommandService videoService;


    @Autowired
    public VideoController(VideoDtoMapper mapper, VideoCommandService videoService) {
        this.mapper = mapper;
        this.videoService = videoService;
    }


    /** save video and add movie video
     * @param newVideo
     * @return
     */
    @PostMapping
    public ResponseEntity<Video> save(@RequestBody AddVideoReq newVideo) {
        return ok(mapper.videoEntitySavetoVideoDto(videoService.save(newVideo)));
    }


    /** add video and upload
     * @param file
     * @param newVideo
     * @return
     * @RequestBody gakbisa
     */
    @PostMapping("/addUpload")
    public ResponseEntity<Video> addVideoAndUpload(@RequestPart("file") MultipartFile file,
            @RequestPart("body") AddVideoReq newVideo) {
        return ok(mapper.videoEntityToVideoDto(videoService.addVideoAndUpload(newVideo, file)));
    }


    /** delete video
     *
     * @param videoId
     * @return string
     * author: lintangbs
     */
    @DeleteMapping("/{videoId}")
    public ResponseEntity<String> deletevideo(
                                                      @PathVariable(value = "videoId") int videoId){
        return ok(videoService.deleteVideo(videoId));
    }


    /** update video
     * @param videoId
     * @param newVideo
     * @return
     */
    @PutMapping("/{videoId}")
    public ResponseEntity<Video> updateVideo(
                                                      @PathVariable(value = "videoId") int videoId,
                                                    @RequestBody  UpdateVideoReq newVideo) {
        return ok(mapper.videoEntityToVideoDto(videoService.updateVideo( videoId, newVideo)));
    }

}
