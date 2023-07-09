package com.lintang.netflik.orderservice.broker.listener;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lintang.netflik.orderservice.broker.message.AddSubscriptionErrorMessage;
import com.lintang.netflik.orderservice.broker.message.AddedSubscriptionMessage;
import com.lintang.netflik.orderservice.broker.message.OutboxMessage;
import com.lintang.netflik.orderservice.command.service.OrderSagaService;
import com.lintang.netflik.orderservice.entity.OutboxEventType;
import com.lintang.netflik.orderservice.entity.SagaStatus;
import com.lintang.netflik.orderservice.util.LocalDateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.apache.commons.lang3.StringUtils;

@Component
public class SagaAddSubscriptionResponseListener {
    private static final Logger LOG = LoggerFactory.getLogger(SagaAddSubscriptionResponseListener.class);
    private LocalDateTimeUtil localDateTimeUtil = new LocalDateTimeUtil();
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private OrderSagaService sagaService;

    /*
    Step 3 saga: get message from  subscription service. Sending message to order.request topic
    */

    @KafkaListener(topics = "t.saga.order.outbox.subscription.response", containerFactory = "stringDeserializerContainerFactory")
    public void onAddedSubscription(@Header(name = KafkaHeaders.RECEIVED_TIMESTAMP) long timestamp,
                                    @Payload String message) throws JsonMappingException, JsonProcessingException {
        var outboxMessage = objectMapper.readValue(message, OutboxMessage.class);
        LOG.debug("step 3 saga :get message from subscription service !");

        if (StringUtils.equalsAny(outboxMessage.getPayload().getEventType(), OutboxEventType.ADDED_SUBSCRIPTION)
         && outboxMessage.getPayload().getSagaStatus() == SagaStatus.PROCESSING) {
            var addedSubscriptionMessage = objectMapper.readValue(outboxMessage.getPayload().getPayload(), AddedSubscriptionMessage.class);

            sagaService.sendMessageToOrderRequestTopic(addedSubscriptionMessage);
            LOG.debug("sending message to order.requesst topic!");
        }
        else if(StringUtils.equalsAny(outboxMessage.getPayload().getEventType(), OutboxEventType.SUBSCRIPTION_ERROR)
         && outboxMessage.getPayload().getSagaStatus() == SagaStatus.COMPENSATING) {
            var addSubscriptionErrorMessage = objectMapper.readValue(outboxMessage.getPayload().getPayload(), AddSubscriptionErrorMessage.class);

            sagaService.compensatingOrderAndPayment(addSubscriptionErrorMessage);
            LOG.debug("Compensating Saga. Send event to order-request-topic and send event to payment-request topic." +
                    "order and payment service do compensating transactions");
        }
    }
}

