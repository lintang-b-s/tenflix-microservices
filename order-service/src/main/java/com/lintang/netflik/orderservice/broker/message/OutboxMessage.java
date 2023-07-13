package com.lintang.netflik.orderservice.broker.message;

import com.lintang.netflik.orderservice.entity.SagaStatus;

import java.util.HashMap;
import java.util.List;


public class OutboxMessage {

    public class Schema {
        public static class Field{
            private String type;
            private boolean optional;
            private String field;



            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public boolean isOptional() {
                return optional;
            }

            public void setOptional(boolean optional) {
                this.optional = optional;
            }


            public String getField() {
                return field;
            }

            public void setField(String field) {
                this.field = field;
            }

        }
        private String type;
        private List<Field> fields;
        private boolean optional;
        private String name;


        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<Field> getFields() {
            return fields;
        }

        public void setFields(List<Field> fields) {
            this.fields = fields;
        }

        public boolean isOptional() {
            return optional;
        }

        public void setOptional(boolean optional) {
            this.optional = optional;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
    public class Payload{
        private String eventType;
        // json string, with dynamic json structure depends on changed data
        private String payload;
        private String id;
        private String sagaStatus;

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

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSagaStatus() {
            return sagaStatus;
        }

        public void setSagaStatus(String sagaStatus) {
            this.sagaStatus = sagaStatus;
        }
    }



    private Schema schema;

    public Schema getSchema() {
        return schema;
    }

    public void setSchema(Schema schema) {
        this.schema = schema;
    }

    private Payload payload;
    public Payload getPayload() {return payload;}
    public void setPayload(Payload payload) {this.payload = payload;}
}



