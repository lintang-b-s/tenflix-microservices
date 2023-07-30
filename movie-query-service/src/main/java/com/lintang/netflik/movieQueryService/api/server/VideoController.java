package com.lintang.netflik.movieQueryService.api.server;


import com.lintang.netflik.movieQueryService.api.request.IncrementViewReq;
import com.lintang.netflik.movieQueryService.api.respone.GetVideoResponse;
import com.lintang.netflik.movieQueryService.broker.message.Video;
import com.lintang.netflik.movieQueryService.command.service.VideoCommandService;

import com.lintang.netflik.movieQueryService.util.DtoMapper.VideoDtoMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/v1/movie-query/videos")
@AllArgsConstructor
@PreAuthorize("hasAuthority('ROLE_user')")
public class VideoController {
    private VideoDtoMapper mapper;
    private VideoCommandService videoService;


    // @Summary     Get all videos by movie id
// @Description   Get all videos by movie id
// @ID          get-all-videos-by-movie-id
// @Tags  	    videp
// @Accept      json
// @Produce     json
// @Param       path variable movie id
// @Success     200 {object} ResponseEntity<List<Video>>
// @Failure     400 {object} response
// @Failure     500 {object} response
// @Router      /api/v1/movie-query/videos/{movieId} [get]
// Author: https://github.com/lintang-b-s
    @GetMapping("/{movieId}")
    public ResponseEntity<List<Video>> getVideosByMovieId(@PathVariable(value = "movieId") int movieId, @AuthenticationPrincipal Jwt principal) {
        return ok(mapper.toListModel(videoService.getVideosByMovieId(movieId, principal.getSubject())));
    }



    // @Summary    Get video publicId and video Offset , untuk streaming video  pakai video player api cloudinary
// @Description  Get video publicId and video Offset , untuk streaming video   pakai video player api cloudinary
// @ID          get-video-by-movieId-andId
// @Tags  	    videp
// @Accept      json
// @Produce     json
// @Param       path variable movieId, videoId
// @Success     200 {object} ResponseEntity<GetVideoResponse>
// @Failure     400 {object} response
// @Failure     500 {object} response
// @Router      /api/v1/movie-query/videos/{movieId}/{videoId} [get]
// Author: https://github.com/lintang-b-s
    @GetMapping("/{movieId}/{videoId}")
    public ResponseEntity<GetVideoResponse> getVideoByMovieIdAndId(@PathVariable(value = "movieId") int movieId,
                                                                   @PathVariable(value = "videoId") int videoId , @AuthenticationPrincipal Jwt principal){
        int videoOffset = videoService.getVideoOffset(videoId, principal.getSubject());
        Video video = mapper.videoEntityToVideoDto(videoService.getVideoByMovieIdAndId(movieId, videoId, principal.getSubject()));
        return ok(GetVideoResponse.builder().videoOffset(videoOffset).video(video).build());
    }




    // @Summary    Set new video offset after user click exit button
// @Description   Set new video offset after user click exit button
// @ID          set-new-view-offset
// @Tags  	    videp
// @Accept      json
// @Produce     json
// @Param       path variable movieId, videoId, request body IncrementViewReq
// @Success     200 {object} ResponseEntity<String>
// @Failure     400 {object} response
// @Failure     500 {object} response
// @Router      /api/v1/movie-query/videos/{movieId}/{videoId}/view [put]
// Author: https://github.com/lintang-b-s
    @PutMapping("/{movieId}/{videoId}/view")
    public ResponseEntity<String> setNewViewOffset(  @PathVariable(value = "videoId") int videoId, @RequestBody IncrementViewReq request) {

        videoService.setNewViewOffset(videoId, request.getUserId(), request.getNewOffset());
        return ok("");
    }


}
