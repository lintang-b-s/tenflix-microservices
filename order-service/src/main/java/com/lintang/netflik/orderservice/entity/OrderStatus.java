package com.lintang.netflik.orderservice.entity;

// CANCELLED = status payment dari midtrans fraud(true) / rejected
public enum OrderStatus {
    PENDING, CANCELLED, PAID, COMPLETED
}
