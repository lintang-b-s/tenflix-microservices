package com.lintang.netflik.orderservice.repository;

import com.lintang.netflik.orderservice.entity.OrderEntity;
import com.lintang.netflik.orderservice.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderJpaRepository extends JpaRepository<OrderEntity, UUID> {
    @Query("update OrderEntity o SET o.orderStatus = :orderStatus where o.id = :id ")
    public OrderEntity updateOrderStatusById(UUID id, String orderStatus);


    @Query("select o from OrderEntity o where o.userId=?1 and o.orderStatus=?2")
    public Optional<List<OrderEntity>> findByUserId(UUID userId, OrderStatus orderStatus);

    public boolean existsByUserIdAndOrderStatus(UUID userId, OrderStatus orderStatus);

}
