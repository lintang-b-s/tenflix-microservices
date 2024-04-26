package com.lintang.netflik.movieQueryService.command.action;


import com.lintang.netflik.models.GetActiveSubscriptionDto;
import com.lintang.netflik.models.GetUserCurrentSubscriptionRequest;
import com.lintang.netflik.models.SubscriptionServiceGrpc;
import com.lintang.netflik.movieQueryService.broker.message.AddVideoMessage;
import com.lintang.netflik.movieQueryService.broker.message.DeleteVideoMessage;
import com.lintang.netflik.movieQueryService.entity.MovieEntity;
import com.lintang.netflik.movieQueryService.entity.Platform;
import com.lintang.netflik.movieQueryService.entity.VideoEntity;
import com.lintang.netflik.movieQueryService.entity.ViewEntity;
import com.lintang.netflik.movieQueryService.exception.ResourceNotFoundException;
import com.lintang.netflik.movieQueryService.exception.UnauthorizedError;
import com.lintang.netflik.movieQueryService.repository.MovieRepository;
import com.lintang.netflik.movieQueryService.repository.VideoRepository;
import com.lintang.netflik.movieQueryService.repository.ViewRepository;
import com.lintang.netflik.movieQueryService.util.entityMapper.ActorEntityMapper;
import com.lintang.netflik.movieQueryService.util.entityMapper.CreatorEntityMapper;
import com.lintang.netflik.movieQueryService.util.entityMapper.MovieEntityMapper;
import com.lintang.netflik.movieQueryService.util.entityMapper.VideoEntityMapper;
import com.lintang.netflik.movieQueryService.util.eventMapper.MovieEventMapper;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators.Add;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException.Unauthorized;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class VideoCommandAction {

    private MovieRepository movierepository;
    private ActorEntityMapper actorEntityMapper;
    private CreatorEntityMapper creatorEntityMapper;
    private MovieEntityMapper movieEntityMapper;
    private VideoEntityMapper videoEntityMapper;
    private VideoRepository videoRepository;
    private MovieEventMapper movieEventMapper;

    private ViewRepository viewRepository;

    @GrpcClient("subscription-service")
    private SubscriptionServiceGrpc.SubscriptionServiceBlockingStub subscriptionStub;



    @Autowired
    public VideoCommandAction(
        MovieRepository movieRepository,
        ActorEntityMapper actorEntityMapper,
        CreatorEntityMapper creatorEntityMapper,
        MovieEntityMapper movieEntityMapper,
        VideoEntityMapper videoEntityMapper,
        VideoRepository videoRepository,
        MovieEventMapper movieEventMapper,
        ViewRepository viewRepository
    ){
        this.movierepository = movieRepository;
        this.actorEntityMapper = actorEntityMapper;
        this.creatorEntityMapper = creatorEntityMapper;
        this.movieEntityMapper = movieEntityMapper;
        this.videoEntityMapper = videoEntityMapper;
        this.videoRepository = videoRepository;
        this.movieEventMapper  = movieEventMapper;
        this.viewRepository  = viewRepository;
    }

    public VideoEntity addVideoToMovie(AddVideoMessage videoMessage) {
//         id video di movie-service sama id video di movie-query-service harus sama
        VideoEntity video = videoRepository.save(videoEntityMapper.saveMessageEntity(videoMessage));
        Optional<MovieEntity> movie = movierepository.findById(Integer.parseInt(videoMessage.getMovieId()));
        if (!movie.isPresent()){
            throw new ResourceNotFoundException("movie with id: " + videoMessage.getMovieId() + " not found");
        }
        MovieEntity movieNew = movie.get();
        video.addMovie(movieNew);
        videoRepository.save(video);
        movieNew.addVideos(video);
        movierepository.save(movieNew);
        return video;
    }


    public void updateVideo(AddVideoMessage videoMessage) {
        Optional<VideoEntity> video = videoRepository.findById(Integer.valueOf(videoMessage.getId()));
        if (!video.isPresent()){
            throw new ResourceNotFoundException("video with id: "+ videoMessage.getId() + " not found!");
        }
        VideoEntity updateVideo =  video.get();
        updateVideo.setPublicId(videoMessage.getPublicId());
        updateVideo.setUrl(videoMessage.getUrl());
        updateVideo.setLength(videoMessage.getLength());
        updateVideo.setTitle(videoMessage.getTitle());
        updateVideo.setSynopsis(videoMessage.getSynopsis());

        //         videoMessage.getMovieId() selalu ada,
        MovieEntity oldMovie= null;MovieEntity movieNew=null;
        if (Integer.parseInt(videoMessage.getMovieId()) != updateVideo.getMovie().getId()) {
            Optional<MovieEntity> oldMovieOp = movierepository.findById(updateVideo.getMovie().getId());
            if (!oldMovieOp.isPresent()){
                throw new ResourceNotFoundException("movie with id: " + updateVideo.getMovie().getId() + " not found");
            }
            oldMovie = oldMovieOp.get();
            Optional<MovieEntity> movie = movierepository.findById(Integer.parseInt(videoMessage.getMovieId()));
            if (!movie.isPresent()){
                throw new ResourceNotFoundException("movie with id: " + videoMessage.getMovieId() + " not found");
            }
             movieNew = movie.get();
            updateVideo.setMovie(movieNew);
        }

        videoRepository.save(updateVideo); // save updated video
        if (Integer.parseInt(videoMessage.getMovieId()) != updateVideo.getMovie().getId()) {
            oldMovie.deleteVideo(updateVideo);
            movierepository.save(oldMovie);
            movieNew.addVideos(updateVideo);
            movierepository.save(movieNew);
        }

        return;
    }

    public void addVideoAndUpload(AddVideoMessage videoMessage) {
        VideoEntity video = videoRepository.save(videoEntityMapper.saveMessageEntityWithoutUrl(videoMessage));
        Optional<MovieEntity> movie = movierepository.findById(Integer.parseInt(videoMessage.getMovieId()));
        if (!movie.isPresent()){
            throw new ResourceNotFoundException("movie with id: " + videoMessage.getMovieId() + " not found");
        }
        MovieEntity movieNew = movie.get();
        video.addMovie(movieNew);
        videoRepository.save(video);
        movieNew.addVideos(video);
        movierepository.save(movieNew);
        return ;
    }

    public void updateVideoUrl(AddVideoMessage videoMessage) {
        Optional<VideoEntity> video = videoRepository.findById(Integer.valueOf(videoMessage.getId()));
        if (!video.isPresent()){
            throw new ResourceNotFoundException("video with id: "+ videoMessage.getId() + " not found!");
        }

        VideoEntity updateVideo =  video.get();
        updateVideo.setUrl(videoMessage.getUrl());
        videoRepository.save(updateVideo); // save updated video
        return;
    }


    public Iterable<VideoEntity> getVideosByMovieId(@NotNull @Valid int movieId, String userId) {
        GetUserCurrentSubscriptionRequest req =
                GetUserCurrentSubscriptionRequest.newBuilder().setGetSubscriptionRequestDto(
                                GetActiveSubscriptionDto.newBuilder().setUserId(userId)
                                        .build()
                        )
                        .build();
        try{
            this.subscriptionStub.getUserCurrentSubscription(req);

        } catch (StatusRuntimeException e) {
            if (e.getStatus().getCode() == Status.Code.FAILED_PRECONDITION){
                throw new UnauthorizedError(e.getMessage());
            }
        }
        return videoRepository.findByMovieId(movieId);
    }

    public VideoEntity getVideoByMovieIdAndId(@NotNull @Valid int movieId,@NotNull @Valid int videoId, String userId) {
        GetUserCurrentSubscriptionRequest req =
                GetUserCurrentSubscriptionRequest.newBuilder().setGetSubscriptionRequestDto(
                                GetActiveSubscriptionDto.newBuilder().setUserId(userId)
                                        .build()
                        )
                        .build();
        try{
            this.subscriptionStub.getUserCurrentSubscription(req);

        } catch (StatusRuntimeException e) {
            if (e.getStatus().getCode() == Status.Code.FAILED_PRECONDITION){
                throw new UnauthorizedError(e.getMessage());
            }
        }

        return videoRepository.getVideoEntitiesByMovie_IdAndId(movieId, videoId).get();
    }

    public void deleteVideo(DeleteVideoMessage videoMessage) {
        VideoEntity video = videoRepository.findById(videoMessage.getId()).get();
        videoRepository.delete(video);
        return;
    }

    public int getViewOffset(int videoId, String userId) {
        VideoEntity video = videoRepository.findById(videoId).get();

        Optional< ViewEntity> viewOp = this.viewRepository.findByVideoIdAndUserId(videoId,userId);
        if (!viewOp.isPresent()) {
            ViewEntity newViewEn = ViewEntity.builder().userId(userId).video(video)
                    .offset(0).platform(Platform.WEB).createdTimeStamp(LocalDateTime.now())
                    .build();
            ViewEntity newView = this.viewRepository.save(newViewEn);
            return newView.getOffset();
        }

        ViewEntity view = viewOp.get();
        return view.getOffset();


    }

    public void setNewViewOffset(int videoId, String userId, int newOffset) {
        ViewEntity view = this.viewRepository.findByVideoIdAndUserId(videoId, userId).get();
        view.setOffset(newOffset);
        this.viewRepository.save(view);
    }
}
