package com.lintang.netflik.orderservice.broker.listener;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lintang.netflik.orderservice.broker.message.AddSubscriptionErrorMessage;
import com.lintang.netflik.orderservice.broker.message.CompensatingOrderSubscriptionMessage;
import com.lintang.netflik.orderservice.broker.message.CompleteOrderMessage;
import com.lintang.netflik.orderservice.broker.message.OutboxMessage;
import com.lintang.netflik.orderservice.command.service.OrderSagaService;
import com.lintang.netflik.orderservice.entity.OutboxEventType;
import com.lintang.netflik.orderservice.entity.SagaStatus;
import com.lintang.netflik.orderservice.util.LocalDateTimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class SagaOrderCompleteListener {
    private static final Logger LOG = LoggerFactory.getLogger(SagaOrderCompleteListener.class);
    private LocalDateTimeUtil localDateTimeUtil = new LocalDateTimeUtil();
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private OrderSagaService sagaService;

    /*
    Step 5 saga: get message from order-requeest topic. Update order status in order db to COMPLETED
    */

    @KafkaListener(topics = "t.saga.order.outbox.order.request",containerFactory = "stringDeserializerContainerFactory")
    public void onAddedSubscription(@Header(name = KafkaHeaders.RECEIVED_TIMESTAMP) long timestamp,
                                    @Payload String message) throws JsonMappingException, JsonProcessingException {
        var outboxMessage=  objectMapper.readValue(message, OutboxMessage.class);
        LOG.debug("step 5 saga: get message from order.request topic!!");
        if (StringUtils.equalsAny(outboxMessage.getPayload().getEventType(), OutboxEventType.COMPLETE_ORDER)
                && outboxMessage.getPayload().getSagaStatus() == SagaStatus.SUCCEEDED
        ) {

            var completeOrderMessage = objectMapper.readValue(outboxMessage.getPayload().getPayload(),
                    CompleteOrderMessage.class);
            sagaService.updateOrderStatusToCompleted(completeOrderMessage);
            LOG.debug("Updating order: " + completeOrderMessage.getOrder().getId() + " Status to COMPLETED");
            LOG.debug(" processOrderSaga SUCEEDEED!");
        }
        if (StringUtils.equalsAny(outboxMessage.getPayload().getEventType(),
                OutboxEventType.COMPENSATING_ORDER_SUBSCRIPTION_ERROR)
         && outboxMessage.getPayload().getSagaStatus() == SagaStatus.COMPENSATING) {

            var addSubscriptionErrorMessage = objectMapper.readValue(
                    outboxMessage.getPayload().getPayload(),
                    CompensatingOrderSubscriptionMessage.class);

            sagaService.compensatingOrder(addSubscriptionErrorMessage);
            LOG.debug("Compensating transactions. Set OrderStatus order "
                    + addSubscriptionErrorMessage.getOrder().getId()+" to PENDING");

        }
    }

}
