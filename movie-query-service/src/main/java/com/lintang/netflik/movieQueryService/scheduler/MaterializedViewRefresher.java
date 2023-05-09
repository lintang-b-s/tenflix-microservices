//package com.lintang.netflik.movieQueryService.scheduler;
//
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.persistence.EntityManager;
//
//@Component
//public class MaterializedViewRefresher {
//    @Autowired
//    private EntityManager entityManager;
//
//    @Transactional
//    @Scheduled(fixedRate = 5000L)
//    public void refresh() {
//        this.entityManager.createNativeQuery("call refresh_movie_mat_view();").executeUpdate();
//    }
//}
