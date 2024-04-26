package com.lintang.netflik.movieQueryService.command.action;


import com.lintang.netflik.movieQueryService.broker.message.AddMovieMessage;
import com.lintang.netflik.movieQueryService.entity.*;
import com.lintang.netflik.movieQueryService.exception.ResourceNotFoundException;
import com.lintang.netflik.movieQueryService.repository.MovieRepository;
import com.lintang.netflik.movieQueryService.repository.VideoRepository;
import com.lintang.netflik.movieQueryService.util.entityMapper.ActorEntityMapper;
import com.lintang.netflik.movieQueryService.util.entityMapper.CreatorEntityMapper;
import com.lintang.netflik.movieQueryService.util.entityMapper.MovieEntityMapper;
import com.lintang.netflik.movieQueryService.util.entityMapper.VideoEntityMapper;
import com.lintang.netflik.movieQueryService.util.eventMapper.MovieEventMapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Component
public class MovieCommandAction {

    private MovieRepository repository;
    private ActorEntityMapper actorEntityMapper;
    private CreatorEntityMapper creatorEntityMapper;
    private MovieEntityMapper movieEntityMapper;
    private VideoEntityMapper videoEntityMapper;
    private VideoRepository videoRepository;
    private MovieEventMapper movieEventMapper;

    //  constructor to inject dependencies
    @Autowired
    public MovieCommandAction(MovieRepository repository,
        ActorEntityMapper actorEntityMapper,
        CreatorEntityMapper creatorEntityMapper,
        MovieEntityMapper movieEntityMapper,
        VideoEntityMapper videoEntityMapper,
        VideoRepository videoRepository,
        MovieEventMapper movieEventMapper
    ) {
        this.repository = repository;
        this.actorEntityMapper = actorEntityMapper;
        this.creatorEntityMapper = creatorEntityMapper;
        this.movieEntityMapper = movieEntityMapper;
        this.videoEntityMapper = videoEntityMapper;
        this.videoRepository = videoRepository;
        this.movieEventMapper = movieEventMapper;
    }


    public MovieEntity addMovie(AddMovieMessage newMovie) {
        MovieEntity newMovieEntity = movieEntityMapper.addMovieEventToEntity(newMovie);

        newMovieEntity.setVideos(
                newMovie.getVideos().stream().map(
                        video -> {
                            VideoEntity getVideo;
                            VideoEntity videoEntity = videoEntityMapper.videoDtoToEntity(video, newMovieEntity);
                            Optional<VideoEntity> videoEntityFromDb = videoRepository.findById(videoEntity.getId());
                            if (!videoEntityFromDb.isPresent()) {
                                getVideo = videoRepository.save(videoEntity);
                            } else {
                                getVideo = videoEntityFromDb.get();

                            }
                            getVideo.addMovie(newMovieEntity);
                            return getVideo;
                        }
                ).collect(Collectors.toSet())
        );


        MovieEntity savedMovie = repository.save(newMovieEntity);
        return savedMovie;
    }



    public MovieEntity updateMovie( int movieId ,AddMovieMessage newMovie) {

        Optional<MovieEntity> movie = repository.findById(movieId);
        if (!movie.isPresent()) {
            throw new ResourceNotFoundException("movie with id: " + movieId+  " not found!!" );
        }
        MovieEntity getMovie = movie.get();
        MovieEntity updatedMovie = movieEntityMapper.updateMovietoEntity( newMovie,getMovie);
        updatedMovie.setVideos(
                newMovie.getVideos().stream().map(
                        video -> {
                            VideoEntity getVideo;
                            VideoEntity videoEntity = videoEntityMapper.videoDtoToEntity(video, updatedMovie);
                            Optional<VideoEntity>  videoEntityFromDb = videoRepository.findById(videoEntity.getId());
                            if (!videoEntityFromDb.isPresent()) {
                                getVideo = videoRepository.save(videoEntity);
                            } else {
                                getVideo = videoEntityFromDb.get();
                            }
                            getVideo.addMovie(updatedMovie);
                            return getVideo;
                        }
                ).collect(Collectors.toSet())
        );

        updatedMovie.setId(newMovie.getId());

        return repository.save(updatedMovie);
    }





    public String deleteMovie( int movieId) {
        Optional<MovieEntity> movie = repository.findById(movieId);
        if (!movie.isPresent()) {
            throw new ResourceNotFoundException("movie with id: " + movieId + " not found !" );
        }
        MovieEntity movieEntity = movie.get();
        Set<ActorEntity> emptyActor = new HashSet<>();
        Set<CreatorEntity> emptyCreator = new HashSet<>();
        Set<TagEntity> emptyTag = new HashSet<>();
        Set<CategoryEntity> emptyCategory = new HashSet<>();

        movieEntity.setActors(emptyActor);
        movieEntity.setCreators(emptyCreator);
        movieEntity.setCategories(emptyCategory);
        movieEntity.setTags(emptyTag);
//        movieEntity.getActors().stream().map(actor -> {
//
//            actor.removeMovie(movieEntity);
//            return actor;
//        });
//        movieEntity.getCreators().stream().map(creator -> {
//
//            creator.removeMovie(movieEntity);
//            return creator;
//        });


        List<VideoEntity> videoEntities = movieEntity.getVideos().stream().map(
                videoEntity -> {
                    videoRepository.delete(videoEntity);
                    return videoEntity;
                }
        ).collect(Collectors.toList());



        MovieEntity deletedMovie = repository.save(movieEntity);
        repository.deleteById(movieId);
        String res = "movie deleted!";
        return res;
    }

}
