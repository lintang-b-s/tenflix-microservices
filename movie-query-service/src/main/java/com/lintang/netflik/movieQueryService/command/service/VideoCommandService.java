package com.lintang.netflik.movieQueryService.command.service;

import com.lintang.netflik.movieQueryService.broker.message.AddVideoMessage;
import com.lintang.netflik.movieQueryService.broker.message.DeleteVideoMessage;
import com.lintang.netflik.movieQueryService.command.action.VideoCommandAction;
import com.lintang.netflik.movieQueryService.entity.VideoEntity;
import com.lintang.netflik.movieQueryService.repository.MovieRepository;
import com.lintang.netflik.movieQueryService.repository.VideoRepository;
import com.lintang.netflik.movieQueryService.util.entityMapper.VideoEntityMapper;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Slf4j
@Service
public class VideoCommandService {

    private VideoCommandAction videoCommandAction;

    @Autowired
    public VideoCommandService(
        VideoCommandAction videoCommandAction
    ) {
        this.videoCommandAction = videoCommandAction;
    }


    public Iterable<VideoEntity> getVideosByMovieId(@NotNull @Valid int movieId, String userId) {

        return videoCommandAction.getVideosByMovieId(movieId, userId);
    }

    public VideoEntity getVideoByMovieIdAndId(@NotNull @Valid int movieId,@NotNull @Valid int videoId, String userId) {
//        videoCommandAction
        VideoEntity video =  videoCommandAction.getVideoByMovieIdAndId(movieId, videoId, userId);
        return  video;
    }
    public int getVideoOffset(int videoId, String userId){
        int videoOffset =  videoCommandAction.getViewOffset(videoId, userId);
        return videoOffset;
    }

    public void setNewViewOffset(int videoId, String userId, int newOffset) {
        videoCommandAction.setNewViewOffset(videoId, userId, newOffset);
    }


    @Transactional
    public VideoEntity addVideoToMovie(AddVideoMessage videoMessage) {
         VideoEntity video= videoCommandAction.addVideoToMovie(videoMessage);
        return video;
    }


    @Transactional
    public void updateVideoFromMovie(AddVideoMessage videoMessage) {
        videoCommandAction.updateVideo(videoMessage);
        return;
    }

    @Transactional
    public void deleteVideoFromMovie(DeleteVideoMessage videoMessage) {
        videoCommandAction.deleteVideo(videoMessage);
        return;
    }

    @Transactional
    public void addVideoAndUpload(AddVideoMessage videoMessage) {
        videoCommandAction.addVideoAndUpload(videoMessage);
        return ;
    }

    @Transactional
    public void updateVideoUrl(AddVideoMessage videoMessage) {
        videoCommandAction.updateVideoUrl(videoMessage);
        return ;
    }

}
