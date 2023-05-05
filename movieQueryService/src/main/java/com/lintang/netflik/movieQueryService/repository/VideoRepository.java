package com.lintang.netflik.movieQueryService.repository;

import com.lintang.netflik.movieQueryService.entity.VideoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface VideoRepository extends JpaRepository<VideoEntity, Integer> {
    @Query("select v from VideoEntity v where v.movie.id = :movieId")
     Iterable<VideoEntity> findVideoEntitiesByMovieEntityId(@Param("movieId") int movieId);

     Optional<VideoEntity> getVideoEntitiesByMovie_IdAndId(int movieId, int videoId);

     String deleteVideoEntitiesByMovie_IdAndId(int movieId, int videoId);

     Optional<VideoEntity> findById(int videoId);

    Optional<VideoEntity> save(VideoEntity videoEntity);


}
