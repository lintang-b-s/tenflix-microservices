package com.lintang.netflik.movieservice.service;

import com.lintang.netflik.movieservice.dto.*;
import com.lintang.netflik.movieservice.entity.ActorEntity;
import com.lintang.netflik.movieservice.entity.CreatorEntity;
import com.lintang.netflik.movieservice.entity.MovieEntity;
import com.lintang.netflik.movieservice.entity.VideoEntity;
import com.lintang.netflik.movieservice.helper.entityMapper.ActorEntityMapper;
import com.lintang.netflik.movieservice.helper.entityMapper.CreatorEntityMapper;
import com.lintang.netflik.movieservice.helper.entityMapper.MovieEntityMapper;
import com.lintang.netflik.movieservice.helper.entityMapper.VideoEntityMapper;
import com.lintang.netflik.movieservice.helper.eventMapper.MovieEventMapper;
import com.lintang.netflik.movieservice.publisher.MovieProducer;
import com.lintang.netflik.movieservice.repository.ActorRepository;
import com.lintang.netflik.movieservice.repository.CreatorRepository;
import com.lintang.netflik.movieservice.repository.MovieRepository;
import com.lintang.netflik.movieservice.repository.VideoRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
public class MovieService {


    private MovieRepository repository;
    private CreatorRepository creatorRepository;
    private ActorEntityMapper actorEntityMapper;
    private CreatorEntityMapper creatorEntityMapper;
    private MovieEntityMapper movieEntityMapper;
    private ActorRepository actorRepository;
    private VideoEntityMapper videoEntityMapper;
    private VideoRepository videoRepository;
    private MovieProducer producer;
    private MovieEventMapper movieEventMapper;


    public MovieService(MovieRepository repository, ActorRepository actorRepository, CreatorRepository creatorRepository,
                        ActorEntityMapper actorEntityMapper,
                        CreatorEntityMapper creatorEntityMapper,
                        MovieEntityMapper movieEntityMapper,
                        VideoEntityMapper videoEntityMapper,
                        VideoRepository videoRepository,
                        MovieProducer producer,
                        MovieEventMapper movieEventMapper) { this.repository = repository;
    this.actorRepository = actorRepository;
    this.creatorRepository= creatorRepository;
    this.actorEntityMapper =actorEntityMapper;
    this.creatorEntityMapper= creatorEntityMapper;
    this.movieEntityMapper = movieEntityMapper;
    this.videoEntityMapper = videoEntityMapper;
    this.videoRepository= videoRepository;
    this.producer = producer;
    this.movieEventMapper= movieEventMapper;
    }


    public MovieEntity addMovie(@Valid AddMovieReq newMovie) {
        MovieEntity newMovieEntity = movieEntityMapper.toEntity(newMovie);
        newMovieEntity.setActors(newMovie.getActors().stream()
                .map(actor -> {
                    ActorEntity actorEntities = actorEntityMapper.toActorEntity(actor);
                    if (actorEntities.getId()>0) {
                        actorEntities = actorRepository.findById(actorEntities.getId()).get();
                        log.info("actorEntities: {}", actorEntities);
                    }
                    actorEntities.addMovie(newMovieEntity);
                    return actorEntities;
                })
                .collect(Collectors.toSet()));

        newMovieEntity.setCreators(newMovie.getCreators().stream()
                .map(creator -> {
                    CreatorEntity creatorEntities = creatorEntityMapper.creatorDtotoCreatorEntity(creator);
                    if (creatorEntities.getId()>0) {
                        creatorEntities = creatorRepository.findById(creatorEntities.getId()).get();
                    }
                    creatorEntities.addMovie(newMovieEntity);
                    return creatorEntities;
                }).collect(Collectors.toSet()));

        if (newMovie.getVideos() != null) {
            newMovieEntity.setVideos(
                    newMovie.getVideos().stream().map(
                            video -> {
                                VideoEntity videoEntity = videoEntityMapper.videoDtoToEntity(video, newMovieEntity);
                                if (videoEntity.getId() > 0) {
                                    videoEntity = videoRepository.findById(videoEntity.getId()).get();
                                }
                                videoEntity.addMovie(newMovieEntity);
                                return videoEntity;
                            }
                    ).collect(Collectors.toSet())
            );
        }


        MovieEntity savedMovie = repository.save(newMovieEntity);
        MovieEvent event = new MovieEvent();
        event.setStatus("PENDING");
        event.setMessageContentBody(savedMovie.getSynopsis());
        event.setMovieTitle(savedMovie.getName());
        event.setImageUrl(savedMovie.getImage());
        producer.sendMessageEmail(event);
        producer.sendMessageAddMovie(movieEventMapper.movieEntityToAddMovieEvent(savedMovie));
        return savedMovie;
    }



    public Iterable<MovieEntity> getMoviesByUserId( @Valid int userId) {
        return repository.findAll();
    }


    public MovieEntity updateMovie(@NotNull @Valid int movieId,
                                   @NotNull @Valid AddMovieReq newMovie) {

        Optional<MovieEntity> movie = repository.findById(movieId);
        if (!movie.isPresent()) {
//            throw new ResourceNotFoundException("invalid movie Id.");
            log.error("error");
        }
        MovieEntity updatedMovie = movie.get();
        Timestamp modelTimestamp = Timestamp.valueOf(newMovie.getrYear().atStartOfDay());

        updatedMovie.setName(newMovie.getName()).setrYear(modelTimestamp)
                .setIdmbRating(newMovie.getIdmbRating()).setMpaRating(newMovie.getMpaRating())
                .setSynopsis(newMovie.getSynopsis()).setType(newMovie.getType())
                ;
        updatedMovie.setActors(newMovie.getActors().stream()
                .map(actor -> {
                    ActorEntity actorEntities = actorEntityMapper.toActorEntity(actor);
                    if (actorEntities.getId()>0) {
                        actorEntities = actorRepository.findById(actorEntities.getId()).get();
                        log.info("actorEntities: {}", actorEntities);
                    }
                    actorEntities.addMovie(updatedMovie);
                    return actorEntities;
                })
                .collect(Collectors.toSet()));

        updatedMovie.setCreators(newMovie.getCreators().stream()
                .map(creator -> {
                    CreatorEntity creatorEntities = creatorEntityMapper.creatorDtotoCreatorEntity(creator);
                    if (creatorEntities.getId()>0) {
                        creatorEntities = creatorRepository.findById(creatorEntities.getId()).get();
                    }
                    creatorEntities.addMovie(updatedMovie);
                    return creatorEntities;
                }).collect(Collectors.toSet()));

        if (newMovie.getVideos() != null) {
            updatedMovie.setVideos(
                    newMovie.getVideos().stream().map(
                            video -> {
                                VideoEntity videoEntity = videoEntityMapper.videoDtoToEntity(video, updatedMovie);
                                if (videoEntity.getId() > 0) {
                                    videoEntity = videoRepository.findById(videoEntity.getId()).get();
                                }
                                videoEntity.addMovie(updatedMovie);
                                return videoEntity;
                            }
                    ).collect(Collectors.toSet())
            );
        }

        producer.sendMessageUpdateMovie(movieEventMapper.movieEntityToAddMovieEvent(updatedMovie));
        return repository.save(updatedMovie);
    }


    public MovieEntity getMovieById(@NotNull @Valid int movieId) {
        Optional<MovieEntity> entity = repository.findById(movieId);
        if (!entity.isPresent()) {
            log.error("error");
        }
        MovieEntity movie = entity.get();
        return movie;
    }


    public String deleteMovie(@NotNull @Valid int movieId) {
        Optional<MovieEntity> movie = repository.findById(movieId);
        if (!movie.isPresent()) {
            log.error("movie not found");
        }
        MovieEntity movieEntity = movie.get();
        Set<ActorEntity> emptyActor = new HashSet<>();
        Set<CreatorEntity> emptyCreator = new HashSet<>();
        movieEntity.setActors(emptyActor);
        movieEntity.setCreators(emptyCreator);
        movieEntity.getActors().stream().map(actor -> {

            actor.removeMovie(movieEntity);
            return actor;
        });
        movieEntity.getCreators().stream().map(creator -> {

            creator.removeMovie(movieEntity);
            return creator;
        });
        if (movieEntity.getVideos() != null ) {
            movieEntity.getVideos().stream().map(video -> {
                video.removeMovie(movieEntity);
                return video;
            });
        }


        MovieEntity deletedMovie = repository.save(movieEntity);
        repository.deleteById(movieId);
        producer.sendMessageDeleteMovie(movieEventMapper.movieEntityToAddMovieEvent(deletedMovie));
        String res = "movie deleted!";
        return res;
    }

}
