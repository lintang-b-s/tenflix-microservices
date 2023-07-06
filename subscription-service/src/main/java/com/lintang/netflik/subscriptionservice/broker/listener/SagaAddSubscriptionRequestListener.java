package com.lintang.netflik.subscriptionservice.broker.listener;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lintang.netflik.subscriptionservice.broker.message.AddSubscriptionMessage;
import com.lintang.netflik.subscriptionservice.broker.message.OutboxMessage;
import com.lintang.netflik.subscriptionservice.command.service.SubscriptionSagaService;
import com.lintang.netflik.subscriptionservice.entity.OutboxEventType;
import com.lintang.netflik.subscriptionservice.entity.SagaStatus;
import com.lintang.netflik.subscriptionservice.util.LocalDateTimeUtil;
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
public class SagaAddSubscriptionRequestListener {

    private static final Logger LOG = LoggerFactory.getLogger(SagaAddSubscriptionRequestListener.class);
    private LocalDateTimeUtil localDateTimeUtil = new LocalDateTimeUtil();
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private SubscriptionSagaService sagaService;

    /*
    Step 3 saga:  get message from order service. Add Subscription to user in subscription db
    */
    @KafkaListener(topics = "t.saga.order.outbox.subscription.request", containerFactory = "stringDeserializerContainerFactory")
    public void onAddSubscription(@Header(name = KafkaHeaders.RECEIVED_TIMESTAMP) long timestamp,
                                  @Payload String message) throws JsonMappingException, JsonProcessingException {
        var outboxMessage = objectMapper.readValue(message, OutboxMessage.class);
        LOG.debug("step 3 saga: get message from order service! ");
        if (StringUtils.equalsAny(outboxMessage.getPayload().getEventType(), OutboxEventType.VALIDATED_PAYMENT)
         && outboxMessage.getPayload().getSagaStatus() == SagaStatus.PROCESSING) {
            var  addSubscriptionMessage = objectMapper.readValue(message, AddSubscriptionMessage.class);

            sagaService.addSubscriptionToUser(addSubscriptionMessage);
            LOG.debug("add subscription plan: " + addSubscriptionMessage.getPlanId() + "to user: " + addSubscriptionMessage.getUserId());
            LOG.debug("sending message to order service!");
        }

    }
}
