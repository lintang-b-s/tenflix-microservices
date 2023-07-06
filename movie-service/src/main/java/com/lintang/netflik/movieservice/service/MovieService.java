package com.lintang.netflik.movieservice.service;

import com.lintang.netflik.movieservice.dto.AddMovieReq;
import com.lintang.netflik.movieservice.entity.ActorEntity;
import com.lintang.netflik.movieservice.entity.CreatorEntity;
import com.lintang.netflik.movieservice.entity.MovieEntity;
import com.lintang.netflik.movieservice.entity.VideoEntity;
import com.lintang.netflik.movieservice.helper.entityMapper.ActorEntityMapper;
import com.lintang.netflik.movieservice.helper.entityMapper.CreatorEntityMapper;
import com.lintang.netflik.movieservice.helper.entityMapper.MovieEntityMapper;
import com.lintang.netflik.movieservice.helper.entityMapper.VideoEntityMapper;
import com.lintang.netflik.movieservice.helper.eventMapper.MovieEventMapper;
import com.lintang.netflik.movieservice.outbox.MovieOutboxHelper;
import com.lintang.netflik.movieservice.outbox.model.movieQuery.MovieOutboxMessage;
import com.lintang.netflik.movieservice.publisher.MovieProducer;
import com.lintang.netflik.movieservice.repository.ActorRepository;
import com.lintang.netflik.movieservice.repository.CreatorRepository;
import com.lintang.netflik.movieservice.repository.MovieRepository;
import com.lintang.netflik.movieservice.repository.VideoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private MovieProducer producer;
    private MovieEventMapper movieEventMapper;
    private MovieOutboxHelper movieOutboxHelper;


    public MovieService(MovieRepository repository, ActorRepository actorRepository, CreatorRepository creatorRepository,
                        ActorEntityMapper actorEntityMapper,
                        CreatorEntityMapper creatorEntityMapper,
                        MovieEntityMapper movieEntityMapper,
                        VideoEntityMapper videoEntityMapper,
                        VideoRepository videoRepository,
                        MovieProducer producer,
                        MovieEventMapper movieEventMapper,
                        MovieOutboxHelper movieOutboxHelper) { this.repository = repository;
    this.actorRepository = actorRepository;
    this.creatorRepository= creatorRepository;
    this.actorEntityMapper =actorEntityMapper;
    this.creatorEntityMapper= creatorEntityMapper;
    this.movieEntityMapper = movieEntityMapper;
    this.videoEntityMapper = videoEntityMapper;
    this.videoRepository= videoRepository;
    this.producer = producer;
    this.movieEventMapper= movieEventMapper;
    this.movieOutboxHelper= movieOutboxHelper;
    }


    public MovieEntity addMovie(@Valid AddMovieReq newMovie) {
        MovieEntity newMovieEntity = movieEntityMapper.toEntity(newMovie);
        newMovieEntity.setActors(newMovie.getActors().stream()
                .map(actor -> {
                    Optional<ActorEntity> actorOptional;
                    ActorEntity getActor;
                    ActorEntity actorEntities = actorEntityMapper.toActorEntity(actor);

                    actorOptional = actorRepository.findById(actorEntities.getId());
                    if (!actorOptional.isPresent()) {
                        getActor=  actorRepository.save(actorEntities);
                    }else {
                        getActor = actorOptional.get();
                    }
                    getActor.addMovie(newMovieEntity);
                    return getActor;
                })
                .collect(Collectors.toSet()));

        newMovieEntity.setCreators(newMovie.getCreators().stream()
                .map(creator -> {
                    Optional<CreatorEntity> creatorOptional;
                    CreatorEntity getCreator;
                    CreatorEntity creatorEntities = creatorEntityMapper.creatorDtotoCreatorEntity(creator);
                    creatorOptional = creatorRepository.findById(creatorEntities.getId());
                    if (!creatorOptional.isPresent() ) {
                        getCreator = creatorRepository.save(creatorEntities);
                    }
                    else {
                        getCreator = creatorOptional.get();
                    }
                    getCreator.addMovie(newMovieEntity);
                    return getCreator;
                }).collect(Collectors.toSet()));

        if (newMovie.getVideos() != null) {
            newMovieEntity.setVideos(
                    newMovie.getVideos().stream().map(
                            video -> {
                                VideoEntity getVideo;
                                Optional<VideoEntity> optionalVid;
                                VideoEntity videoEntity = videoEntityMapper.videoDtoToEntity(video, newMovieEntity);
                                optionalVid = videoRepository.findById(videoEntity.getId());

                                if (!optionalVid.isPresent()) {
                                    getVideo = videoRepository.save(videoEntity);
                                }
                                else {
                                    getVideo = optionalVid.get();
                                }

                                getVideo.addMovie(newMovieEntity);
                                return getVideo;
                            }
                    ).collect(Collectors.toSet())
            );
        }


        MovieEntity savedMovie = repository.save(newMovieEntity);

        MovieOutboxMessage  notificationSavedOutboxMessage =movieOutboxHelper.notificationMovieOutboxMessage(
                movieEventMapper.movieEntityToAddMovieEvent(savedMovie));
        log.info("NotificationOutboxMessage saved with outbox id: {}", notificationSavedOutboxMessage);

        MovieOutboxMessage movieSavedOutboxMessage=
                movieOutboxHelper.createMovieOutboxMessage(movieEventMapper.movieEntityToAddMovieEvent(savedMovie));
        log.info("MovieUpdatedOutboxMessage saved with outbox id: {}", movieSavedOutboxMessage
                .getId());
        return savedMovie;
    }



    public Iterable<MovieEntity> getMoviesByUserId( @Valid int userId) {
        return repository.findAll();
    }


    @Transactional
    public MovieEntity updateMovie(@NotNull @Valid int movieId,
                                   @NotNull @Valid AddMovieReq newMovie) {

        Optional<MovieEntity> movie = repository.findById(movieId);
        if (!movie.isPresent()) {
//            throw new ResourceNotFoundException("invalid movie Id.");
            log.error("error");
        }
        MovieEntity updatedMovie = movie.get();
        Timestamp modelTimestamp = Timestamp.valueOf(newMovie.getrYear().atStartOfDay());

        updatedMovie.setId(movieId).setName(newMovie.getName()).setrYear(modelTimestamp)
                .setIdmbRating(newMovie.getIdmbRating()).setMpaRating(newMovie.getMpaRating())
                .setSynopsis(newMovie.getSynopsis()).setType(newMovie.getType())
                ;
        updatedMovie.setActors(newMovie.getActors().stream()
                .map(actor -> {
                    Optional<ActorEntity> actorOptional;
                    ActorEntity getActor;
                    ActorEntity actorEntities = actorEntityMapper.toActorEntity(actor);
                    actorOptional =actorRepository.findById(actorEntities.getId());
                    if (!actorOptional.isPresent()) {
                        getActor = actorRepository.save(actorEntities);
                    }else {
                        getActor = actorOptional.get();
                    }

                    getActor.addMovie(updatedMovie);
                    return getActor;
                })
                .collect(Collectors.toSet()));

        updatedMovie.setCreators(newMovie.getCreators().stream()
                .map(creator -> {
                    Optional<CreatorEntity> creatorOptional;
                    CreatorEntity getCreator;
                    CreatorEntity creatorEntities = creatorEntityMapper.creatorDtotoCreatorEntity(creator);
                    creatorOptional = creatorRepository.findById(creatorEntities.getId());
                    if (!creatorOptional.isPresent()) {
                        getCreator = creatorRepository.save(creatorEntities);
                    }
                    else {
                        getCreator = creatorOptional.get();
                    }
                    getCreator.addMovie(updatedMovie);
                    return getCreator;
                }).collect(Collectors.toSet()));

        if (newMovie.getVideos() != null) {
            updatedMovie.setVideos(
                    newMovie.getVideos().stream().map(
                            video -> {
                                VideoEntity getVideo;
                                Optional<VideoEntity> optionalVid;
                                VideoEntity videoEntity = videoEntityMapper.videoDtoToEntity(video, updatedMovie);
                                optionalVid = videoRepository.findById(videoEntity.getId());

                                if (!optionalVid.isPresent()) {
                                    getVideo = videoRepository.save(videoEntity);
                                }
                                else {
                                    getVideo = optionalVid.get();
                                }

                                getVideo.addMovie(updatedMovie);
                                return getVideo;
                            }
                    ).collect(Collectors.toSet())
            );
        }

        updatedMovie.setId(movieId);

        MovieEntity savedUpdatedMovie = saveMovie(updatedMovie);
        MovieOutboxMessage movieUpdatedOutboxMessage=
                movieOutboxHelper.updateMovieOutboxMessage(movieEventMapper
                        .movieEntityToAddMovieEvent(savedUpdatedMovie));
        log.info("MovieUpdatedOutboxMessage saved  in updateMovie with outbox id: {} and version: {}", movieUpdatedOutboxMessage
                .getId(), movieUpdatedOutboxMessage.getVersion());

        return savedUpdatedMovie;
    }

    @Transactional
    public MovieEntity saveMovie(MovieEntity movie) {
        return repository.save(movie);
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
        videoRepository.deleteByMovieId(movieEntity.getId());
//       movieEntity.removeVideos();


        MovieEntity deletedMovie = repository.save(movieEntity);
        repository.deleteById(movieId);
        MovieOutboxMessage deleteMovieOutboxMessage = movieOutboxHelper.deleteMovieOutboxMessage(
                movieEventMapper.movieEntityToAddMovieEvent(deletedMovie));
        log.info("deleteMovieOutboxMessage saved with id: {}", deleteMovieOutboxMessage.getId());
        String res = "movie deleted!";
        return res;
    }

}
