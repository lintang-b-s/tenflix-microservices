package com.lintang.netflik.subscriptionservice.entity;

public interface SagaStatus {
    String STARTED = "STARTED";
    String PROCESSING = "PROCESSING";
    String COMPENSATING = "COMPENSATING";
    String COMPENSATED= "COMPENSATED";
    String SUCCEEDED = "SUCCEEDED";
}
