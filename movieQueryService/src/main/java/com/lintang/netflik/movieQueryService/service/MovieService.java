package com.lintang.netflik.movieQueryService.service;

import com.lintang.netflik.movieQueryService.entity.*;
import com.lintang.netflik.movieQueryService.event.AddMovieEvent;
import com.lintang.netflik.movieQueryService.helper.entityMapper.ActorEntityMapper;
import com.lintang.netflik.movieQueryService.helper.entityMapper.CreatorEntityMapper;
import com.lintang.netflik.movieQueryService.helper.entityMapper.MovieEntityMapper;
import com.lintang.netflik.movieQueryService.helper.entityMapper.VideoEntityMapper;
import com.lintang.netflik.movieQueryService.helper.eventMapper.MovieEventMapper;

import com.lintang.netflik.movieQueryService.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
    private MovieEventMapper movieEventMapper;


    public MovieService(MovieRepository repository, ActorRepository actorRepository, CreatorRepository creatorRepository,
                        ActorEntityMapper actorEntityMapper,
                        CreatorEntityMapper creatorEntityMapper,
                        MovieEntityMapper movieEntityMapper,
                        VideoEntityMapper videoEntityMapper,
                        VideoRepository videoRepository,
                        MovieEventMapper movieEventMapper) { this.repository = repository;
    this.actorRepository = actorRepository;
    this.creatorRepository= creatorRepository;
    this.actorEntityMapper =actorEntityMapper;
    this.creatorEntityMapper= creatorEntityMapper;
    this.movieEntityMapper = movieEntityMapper;
    this.videoEntityMapper = videoEntityMapper;
    this.videoRepository= videoRepository;
    this.movieEventMapper= movieEventMapper;

    }


    public MovieEntity addMovie(@Valid AddMovieEvent newMovie) {
        MovieEntity newMovieEntity = movieEntityMapper.addMovieEventToEntity(newMovie);

            newMovieEntity.setVideos(
                    newMovie.getVideos().stream().map(
                            video -> {
                                VideoEntity getVideo;
                                VideoEntity videoEntity = videoEntityMapper.videoDtoToEntity(video, newMovieEntity);
                                Optional<VideoEntity>  videoEntityFromDb = videoRepository.findById(videoEntity.getId());
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


        newMovieEntity.setId(newMovie.getId());
        MovieEntity savedMovie = repository.save(newMovieEntity);

        return savedMovie;
    }



    public Iterable<MovieEntity> getMoviesByUserId(@Valid int userId) {
        return repository.findAll();
    }


    public MovieEntity updateMovie(@NotNull @Valid AddMovieEvent newMovie) {

        Optional<MovieEntity> movie = repository.findById(newMovie.getId());
        if (!movie.isPresent()) {
//            throw new ResourceNotFoundException("invalid movie Id.");
            log.error("error");
        }
        MovieEntity getMovie = movie.get();
        Timestamp modelTimestamp = Timestamp.valueOf(newMovie.getrYear().atStartOfDay());

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

        log.info("videoentitites id: {}", movieEntity.getVideos().stream().map(videoEntity -> String.valueOf(videoEntity.getId())).collect(Collectors.joining(",")));
         List<VideoEntity> videoEntities = movieEntity.getVideos().stream().map(
                videoEntity -> {
                     videoRepository.delete(videoEntity);
                     return videoEntity;
                }
        ).collect(Collectors.toList());
        log.info("delted response string : {}", videoEntities.stream().map(v -> String.valueOf(v) ).collect(Collectors.joining(",")));



        MovieEntity deletedMovie = repository.save(movieEntity);
        repository.deleteById(movieId);
        String res = "movie deleted!";
        return res;
    }

}
