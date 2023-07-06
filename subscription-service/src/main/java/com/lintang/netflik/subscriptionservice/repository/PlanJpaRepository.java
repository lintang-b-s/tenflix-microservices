package com.lintang.netflik.subscriptionservice.repository;

import com.lintang.netflik.subscriptionservice.entity.PlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PlanJpaRepository extends JpaRepository<PlanEntity, Long> {

}
