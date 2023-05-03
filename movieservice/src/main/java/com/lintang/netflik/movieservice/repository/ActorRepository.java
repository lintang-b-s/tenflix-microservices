package com.lintang.netflik.movieservice.repository;

import com.lintang.netflik.movieservice.entity.ActorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.UUID;


public interface ActorRepository extends JpaRepository<ActorEntity, Integer> {
    Optional<ActorEntity> findById(int actorId);

}