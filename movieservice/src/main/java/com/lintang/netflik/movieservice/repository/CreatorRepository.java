package com.lintang.netflik.movieservice.repository;

import com.lintang.netflik.movieservice.entity.CreatorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CreatorRepository  extends JpaRepository<CreatorEntity, Integer> {

    Optional<CreatorEntity> findById(int creatorId);
}
