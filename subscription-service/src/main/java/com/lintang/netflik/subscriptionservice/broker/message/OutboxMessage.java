package com.lintang.netflik.subscriptionservice.broker.message;

import com.lintang.netflik.subscriptionservice.entity.SagaStatus;

public class OutboxMessage {
    public class Payload{
        private String eventType;
        // json string, with dynamic json structure depends on changed data
        private String payload;
        private long id;
        private SagaStatus sagaStatus;

        public String getEventType() {
            return eventType;
        }

        public void setEventType(String eventType) {
            this.eventType = eventType;
        }

        public String getPayload() {
            return payload;
        }

        public void setPayload(String payload) {
            this.payload = payload;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public SagaStatus getSagaStatus() {
            return sagaStatus;
        }

        public void setSagaStatus(SagaStatus sagaStatus) {
            this.sagaStatus = sagaStatus;
        }
    }

    private Payload payload;
    public Payload getPayload() {return payload;}
    public void setPayload(Payload payload) {this.payload = payload;}
}
