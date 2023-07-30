package com.lintang.netflik.movieservice.command.action;


import com.lintang.netflik.movieservice.api.request.AddMovieReq;
import com.lintang.netflik.movieservice.command.service.MovieCommandService;
import com.lintang.netflik.movieservice.entity.*;
import com.lintang.netflik.movieservice.exception.ResourceNotFoundException;

import com.lintang.netflik.movieservice.repository.*;
import com.lintang.netflik.movieservice.util.entityMapper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Component
public class MovieCommandAction {

    private static final Logger LOG = LoggerFactory.getLogger(MovieCommandAction.class);



    private MovieRepository repository;

    private CreatorRepository creatorRepository;

    private ActorEntityMapper actorEntityMapper;
    private CreatorEntityMapper creatorEntityMapper;
    private MovieEntityMapper movieEntityMapper;
    private ActorRepository actorRepository;
    private VideoEntityMapper videoEntityMapper;
    private VideoRepository videoRepository;
    private TagEntityMapper tagEntityMapper;
    private TagRepository tagRepository;
    private CategoryEntityMapper categoryEntityMapper;
    private CategoryRepository categoryRepository;

    public MovieCommandAction(MovieRepository repository, ActorRepository actorRepository, CreatorRepository creatorRepository,
                        ActorEntityMapper actorEntityMapper,
                        CreatorEntityMapper creatorEntityMapper,
                        MovieEntityMapper movieEntityMapper,
                        VideoEntityMapper videoEntityMapper,
                        VideoRepository videoRepository,
                              TagEntityMapper tagEntityMapper,
                              TagRepository tagRepository,
                              CategoryEntityMapper categoryEntityMapper,
                              CategoryRepository categoryRepository) { this.repository = repository;
        this.actorRepository = actorRepository;
        this.creatorRepository= creatorRepository;
        this.actorEntityMapper =actorEntityMapper;
        this.creatorEntityMapper= creatorEntityMapper;
        this.movieEntityMapper = movieEntityMapper;
        this.videoEntityMapper = videoEntityMapper;
        this.videoRepository= videoRepository;
        this.tagEntityMapper = tagEntityMapper;
        this.tagRepository = tagRepository;
        this.categoryEntityMapper = categoryEntityMapper;
        this.categoryRepository = categoryRepository;
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
                        getActor=  actorRepository.save(actorEntityMapper.toEntity(actor.getName()));
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
                        getCreator = creatorRepository.save(creatorEntityMapper.toEntity(creator.getName()));
                    }
                    else {
                        getCreator = creatorOptional.get();
                    }
                    getCreator.addMovie(newMovieEntity);
                    return getCreator;
                }).collect(Collectors.toSet()));

        newMovieEntity.setTags(newMovie.getTags().stream()
                .map(tag -> {
                    Optional<TagEntity> tagOptional;
                    TagEntity getTag;
                    TagEntity tagEntity = tagEntityMapper.toEntity(tag);
                    tagOptional = tagRepository.findById(tagEntity.getId());
                    if (!tagOptional.isPresent()) {
                        getTag = tagRepository.save(tagEntityMapper.toEntity(tag.getName()));
                    }else {
                        getTag = tagOptional.get();
                    }
                    getTag.addMovie(newMovieEntity);
                    return getTag;
                }).collect(Collectors.toSet()));

        newMovieEntity.setCategories(newMovie.getCategories().stream()
                .map(category -> {
                    Optional<CategoryEntity> categoryOptional;
                    CategoryEntity getCategory;
                    CategoryEntity categoryEntity = categoryEntityMapper.toEntity(category);
                    categoryOptional = categoryRepository.findById(categoryEntity.getId());
                    if (!categoryOptional.isPresent()) {
                        getCategory = categoryRepository.save(categoryEntityMapper.toEntity(category.getName()));
                    }else {
                        getCategory = categoryOptional.get();
                    }
                    getCategory.addMovie(newMovieEntity);
                    return getCategory;
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
                                    getVideo = videoRepository.save(videoEntityMapper.videoDtoToEntity(video));
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

        return savedMovie;
    }


    @Transactional
    public MovieEntity updateMovie(@NotNull @Valid int movieId,
                                   @NotNull @Valid AddMovieReq newMovie) {

        Optional<MovieEntity> movie = repository.findById(movieId);
        if (!movie.isPresent()) {
            throw new ResourceNotFoundException("movie with id: " + movie.get().getId() + " not found");
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
                        getActor = actorRepository.save(actorEntityMapper.toEntity(actor.getName()));
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
                        getCreator = creatorRepository.save(creatorEntityMapper.toEntity(creator.getName()));
                    }
                    else {
                        getCreator = creatorOptional.get();
                    }
                    getCreator.addMovie(updatedMovie);
                    return getCreator;
                }).collect(Collectors.toSet()));

        updatedMovie.setTags(newMovie.getTags().stream()
                .map(tag -> {
                    Optional<TagEntity> tagOptional;
                    TagEntity getTag;
                    TagEntity tagEntity = tagEntityMapper.toEntity(tag);
                    tagOptional = tagRepository.findById(tagEntity.getId());
                    if (!tagOptional.isPresent()) {
                        getTag= tagRepository.save(tagEntityMapper.toEntity(tag.getName()));
                    } else {
                        getTag= tagOptional.get();
                    }
                    getTag.addMovie(updatedMovie);
                    return getTag;
                }).collect(Collectors.toSet()));

        updatedMovie.setCategories(newMovie.getCategories().stream()
                .map(category -> {
                    Optional<CategoryEntity> categoryOptional;
                    CategoryEntity getCategory;
                    CategoryEntity categoryEntity = categoryEntityMapper.toEntity(category);
                    categoryOptional = categoryRepository.findById(categoryEntity.getId());
                    if (!categoryOptional.isPresent()) {
                        getCategory = categoryRepository.save(categoryEntityMapper.toEntity(category.getName()));
                    }else {
                        getCategory = categoryOptional.get();
                    }
                    getCategory.addMovie(updatedMovie);
                    return getCategory;
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
                                    getVideo = videoRepository.save(videoEntityMapper.videoDtoToEntity(video));
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


        return savedUpdatedMovie;
    }

    @Transactional
    public MovieEntity saveMovie(MovieEntity movie) {
        return repository.save(movie);
    }


    public MovieEntity getMovieById(@NotNull @Valid int movieId) {
        Optional<MovieEntity> entity = repository.findById(movieId);
        if (!entity.isPresent()) {
            throw new ResourceNotFoundException("movie with id: " + entity.get().getId() + " not found");
        }
        MovieEntity movie = entity.get();
        return movie;
    }


    public String deleteMovie(@NotNull @Valid int movieId) {
        Optional<MovieEntity> movie = repository.findById(movieId);
        if (!movie.isPresent()) {
            throw new ResourceNotFoundException("movie with id: " + movie.get().getId() + " not found");
        }
        MovieEntity movieEntity = movie.get();
        Set<ActorEntity> emptyActor = new HashSet<>();
        Set<CreatorEntity> emptyCreator = new HashSet<>();
        Set<TagEntity> emptyTag = new HashSet<>();
        Set<CategoryEntity> emptyCategory = new HashSet<>();
        movieEntity.setActors(emptyActor);
        movieEntity.setCreators(emptyCreator);
        movieEntity.setTags(emptyTag);
        movieEntity.setCategories(emptyCategory);
        movieEntity.getActors().stream().map(actor -> {

            actor.removeMovie(movieEntity);
            return actor;
        });
        movieEntity.getCreators().stream().map(creator -> {

            creator.removeMovie(movieEntity);
            return creator;
        });
        movieEntity.getTags().stream().map(tag -> {
            tag.removeMovie(movieEntity);
            return tag;
        });
        movieEntity.getCategories().stream().map(category -> {
            category.removeMovie(movieEntity);
            return category;
        });

        videoRepository.deleteByMovieId(movieEntity.getId());
//       movieEntity.removeVideos();


        MovieEntity deletedMovie = repository.save(movieEntity);
        repository.deleteById(movieId);

        String res = "movie deleted!";
        return res;
    }

}
