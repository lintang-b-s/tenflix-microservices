package com.kafkastreams.movieservice.command.action;


import com.kafkastreams.movieservice.api.request.AddVideoReq;
import com.kafkastreams.movieservice.api.request.UpdateVideoReq;
import com.kafkastreams.movieservice.api.response.Video;
import com.kafkastreams.movieservice.entity.MovieEntity;
import com.kafkastreams.movieservice.entity.VideoEntity;
import com.kafkastreams.movieservice.exception.BadRequestException;
import com.kafkastreams.movieservice.exception.ResourceNotFoundException;
import com.kafkastreams.movieservice.query.action.MovieQueryAction;
import com.kafkastreams.movieservice.repository.MovieRepository;
import com.kafkastreams.movieservice.repository.VideoRepository;
import com.kafkastreams.movieservice.util.entityMapper.VideoEntityMapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;


@NoArgsConstructor
@Component
public class VideoCommandAction {

    private VideoRepository repository;
    private VideoEntityMapper mapper;

    private MovieQueryAction movieQueryAction;

    @Autowired
    public VideoCommandAction(VideoRepository repository, VideoEntityMapper mapper,
                              MovieQueryAction movieQueryAction) {
        this.repository = repository;
        this.mapper = mapper;
        this.movieQueryAction = movieQueryAction;
    }

    public VideoEntity save(Video newVideo) {
        return repository.save(mapper.videoDtoToEntity(newVideo));
    }

    public void updateEntityBatch(Set<VideoEntity> video){
         repository.saveAll(video);
    }

    public VideoEntity saveReq(AddVideoReq videoReq){
        Optional<VideoEntity> opVideo = repository.findByTitle(videoReq.getTitle());
        if (opVideo.isPresent()){
            throw new BadRequestException("Video with name: " + videoReq.getTitle() + " already in database");
        }

        MovieEntity movieEntity = movieQueryAction.getMovieById(videoReq.getMovieId());

        return repository.save(mapper.saveEntity(videoReq, movieEntity));
    }

    public void deleteVideoByMovie(int movieID){
        repository.deleteByMovieId(movieID);

    }

    public void delteVideoEntity(VideoEntity video){
        repository.delete(video);
    }

    public Iterable<VideoEntity> getVideosByMovieId( int movieId) {
        return repository.findVideoEntitiesByMovieEntityId(movieId);
    }




    public VideoEntity addVideoAndUpload( AddVideoReq newVideo) {
         Optional<VideoEntity> opVideo = repository.findByTitle(newVideo.getTitle());
         if (opVideo.isPresent()){
            throw new BadRequestException("Video with name: " + newVideo.getTitle() + " already in database");
         }

        MovieEntity movie = movieQueryAction.getMovieById(newVideo.getMovieId());

        return repository.save(mapper.toEntityBeforeUpload(newVideo, movie)); 
    }


    public VideoEntity getVideoByMovieIdAndId( int movieId, int videoId) {
        Optional<VideoEntity> optionalVideo = repository.getVideoEntitiesByMovie_IdAndId(movieId, videoId);
        if (!optionalVideo.isPresent()) {
            throw new ResourceNotFoundException("video dengan id: " + videoId + " tidak ditemukan dalam database");
        }
        return optionalVideo.get();
    }

    public VideoEntity getVideoById(int videoId) {
        Optional<VideoEntity> optionalVid = repository.getVideoEntitiesById(videoId);
        if (!optionalVid.isPresent()) {
            throw new ResourceNotFoundException("video dengan id: " + videoId + " tidak ditemukan dalam database");
        }
        return optionalVid.get();
    }



    public void updateVideoUrl(String url, int videoID) {
        VideoEntity video = repository.findById(videoID).get();
        video.setUrl(url);
        repository.save(video);
        return ;
    }
    public String deleteVideoFromMovie(  int videoId) {
        Optional<VideoEntity> video = repository.findById(videoId);
        if (video.isEmpty()){
            throw new ResourceNotFoundException("video with id" + videoId + " not found!");
        }
        VideoEntity videoGet = video.get();
        videoGet.removeMovie();
        repository.delete( videoGet);
        return "video deleted!";
    }


    public VideoEntity updateVideo( int videoId,

                                             UpdateVideoReq newVideo){

        Optional<VideoEntity> videoFromDb = repository.findById(videoId);
        if (videoFromDb.isEmpty()){
            throw new ResourceNotFoundException("video with id " + videoId + " not found!");
        }
        VideoEntity video = videoFromDb.get();
        video.setUrl(newVideo.getUrl()).setLength(newVideo.getLength())
                .setTitle(newVideo.getTitle()).setSynopsis(newVideo.getSynopsis());


        VideoEntity videoSaved = repository.save(video);



        return videoSaved;

    }
}
