package com.lintang.netflik.orderservice.repository;


import com.lintang.netflik.orderservice.entity.PlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PlanJpaRepository extends JpaRepository<PlanEntity, UUID> {

}
