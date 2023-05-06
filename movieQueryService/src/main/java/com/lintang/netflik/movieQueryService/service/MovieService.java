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
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.HashSet;
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
    private GetAllMoviesRepository getAllMoviesRepository;


    public MovieService(MovieRepository repository, ActorRepository actorRepository, CreatorRepository creatorRepository,
                        ActorEntityMapper actorEntityMapper,
                        CreatorEntityMapper creatorEntityMapper,
                        MovieEntityMapper movieEntityMapper,
                        VideoEntityMapper videoEntityMapper,
                        VideoRepository videoRepository,
                        MovieEventMapper movieEventMapper,
                        GetAllMoviesRepository getAllMoviesRepository
                    ) { this.repository = repository;
    this.actorRepository = actorRepository;
    this.creatorRepository= creatorRepository;
    this.actorEntityMapper =actorEntityMapper;
    this.creatorEntityMapper= creatorEntityMapper;
    this.movieEntityMapper = movieEntityMapper;
    this.videoEntityMapper = videoEntityMapper;
    this.videoRepository= videoRepository;
    this.movieEventMapper= movieEventMapper;
    this.getAllMoviesRepository= getAllMoviesRepository;

    }


    public MovieEntity addMovie(@Valid AddMovieEvent newMovie) {
        MovieEntity newMovieEntity = movieEntityMapper.addMovieEventToEntity(newMovie);
        newMovieEntity.setActors(newMovie.getActors().stream()
                .map(actor -> {
                    ActorEntity getActor;
                    ActorEntity actorEntities = actorEntityMapper.toActorEntity(actor);

                    Optional<ActorEntity> actorEntityFromDb = actorRepository.findById(actorEntities.getId());
                    if (!actorEntityFromDb.isPresent()){
                        actorEntities = actorEntityMapper.toEntity(actor);
                        getActor = actorRepository.save(actorEntities);
                    } else {
                        getActor = actorEntityFromDb.get();

                    }
                    log.info("getActor: {}", getActor.getName());

                    getActor.addMovie(newMovieEntity);
                    return getActor;
                })
                .collect(Collectors.toSet()));

        newMovieEntity.setCreators(newMovie.getCreators().stream()
                .map(creator -> {
                    CreatorEntity getCreator;
                    CreatorEntity creatorEntities = creatorEntityMapper.creatorDtotoCreatorEntity(creator);
                    Optional<CreatorEntity> creatorEntityFromDb = creatorRepository.findById(creatorEntities.getId());
                    if (!creatorEntityFromDb.isPresent()) {
                        getCreator = creatorRepository.save(creatorEntities);
                    } else {
                        getCreator = creatorEntityFromDb.get();

                    }

                    getCreator.addMovie(newMovieEntity);
                    return getCreator;
                }).collect(Collectors.toSet()));

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



        MovieEntity savedMovie = repository.save(newMovieEntity);

        return savedMovie;
    }



    public Iterable<GetAllMovies> getMoviesByUserId(@Valid int userId) {
        return getAllMoviesRepository.findAll();
    }


    public MovieEntity updateMovie(@NotNull @Valid AddMovieEvent newMovie) {

        Optional<MovieEntity> movie = repository.findById(newMovie.getId());
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
                    ActorEntity getActor;
                    ActorEntity actorEntities = actorEntityMapper.toActorEntity(actor);

                    Optional<ActorEntity> actorEntityFromDb = actorRepository.findById(actorEntities.getId());
                    if (!actorEntityFromDb.isPresent()){
                            getActor  = actorRepository.save(actorEntities);
                    } else {
                        getActor = actorEntityFromDb.get();
                    }

                    getActor.addMovie(updatedMovie);
                    return getActor;
                })
                .collect(Collectors.toSet()));

        updatedMovie.setCreators(newMovie.getCreators().stream()
                .map(creator -> {
                    CreatorEntity getCreator;
                    CreatorEntity creatorEntities = creatorEntityMapper.creatorDtotoCreatorEntity(creator);
                    Optional<CreatorEntity> creatorEntityFromDb = creatorRepository.findById(creatorEntities.getId());
                    if (!creatorEntityFromDb.isPresent()) {
                        getCreator = creatorRepository.save(creatorEntities);
                    }
                    else {
                         getCreator = creatorEntityFromDb.get();
                    }

                    getCreator.addMovie(updatedMovie);
                    return getCreator;
                }).collect(Collectors.toSet()));

        updatedMovie.setVideos(
                newMovie.getVideos().stream().map(
                        video -> {
                            VideoEntity getVideo;
                            VideoEntity videoEntity = videoEntityMapper.videoDtoToEntity(video, updatedMovie);
                            Optional<VideoEntity>  videoEntityFromDb = videoRepository.findById(videoEntity.getId());
                            if (!videoEntityFromDb.isPresent()) {
                                getVideo = videoRepository.save(videoEntity);
                            }else {
                                getVideo = videoEntityFromDb.get();
                            }
                            getVideo.addMovie(updatedMovie);
                            return getVideo;
                        }
                ).collect(Collectors.toSet())
        );

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
        String res = "movie deleted!";
        return res;
    }

}
