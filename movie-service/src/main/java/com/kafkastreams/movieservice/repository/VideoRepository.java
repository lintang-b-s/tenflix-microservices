package com.kafkastreams.movieservice.repository;

import com.kafkastreams.movieservice.entity.VideoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface VideoRepository extends JpaRepository<VideoEntity, Integer> {
    @Query("select v from VideoEntity v where v.movie.id = :movieId")
     Iterable<VideoEntity> findVideoEntitiesByMovieEntityId(@Param("movieId") int movieId);

     Optional<VideoEntity> getVideoEntitiesByMovie_IdAndId(int movieId, int videoId);

     String deleteVideoEntitiesByMovie_IdAndId(int movieId, int videoId);

     Optional<VideoEntity> findById(int videoId);

     @Modifying
     @Transactional
     @Query("DELETE FROM VideoEntity v WHERE v.movie.id = ?1")
     void deleteByMovieId(int movieId);

    Optional<VideoEntity> findByTitle(String title);
}
