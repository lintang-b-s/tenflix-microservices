package com.lintang.netflik.subscriptionservice.broker.message;

// CANCELLED = status payment dari midtrans fraud(true) / rejected
public enum OrderStatus {
    PENDING, CANCELLED, PAID, COMPLETED
}
