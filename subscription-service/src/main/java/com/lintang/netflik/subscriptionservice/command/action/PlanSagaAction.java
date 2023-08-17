package com.lintang.netflik.subscriptionservice.command.action;

import com.lintang.netflik.subscriptionservice.entity.PlanEntity;
import com.lintang.netflik.subscriptionservice.repository.PlanJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PlanSagaAction {
    @Autowired
    private PlanJpaRepository planJpaRepository;

    public Optional<PlanEntity> findById(Long planId) {
        return planJpaRepository.findById(planId);
    }
}
