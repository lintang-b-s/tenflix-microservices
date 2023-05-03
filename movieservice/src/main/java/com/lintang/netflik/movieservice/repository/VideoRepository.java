package com.lintang.netflik.movieservice.repository;

import com.lintang.netflik.movieservice.entity.VideoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface VideoRepository extends JpaRepository<VideoEntity, Integer> {
    @Query("select v from VideoEntity v where v.movie.id = :movieId")
     Iterable<VideoEntity> findVideoEntitiesByMovieEntityId(@Param("movieId") int movieId);

     Optional<VideoEntity> getVideoEntitiesByMovie_IdAndId(int movieId, int videoId);

     String deleteVideoEntitiesByMovie_IdAndId(int movieId, int videoId);

     Optional<VideoEntity> findById(int videoId);

}
