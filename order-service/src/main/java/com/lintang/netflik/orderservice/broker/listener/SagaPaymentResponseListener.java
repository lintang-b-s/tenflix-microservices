package com.lintang.netflik.orderservice.broker.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lintang.netflik.orderservice.broker.message.OutboxMessage;
import com.lintang.netflik.orderservice.broker.message.PaymentCanceledMessage;
import com.lintang.netflik.orderservice.broker.message.PaymentValidatedMessage;
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
public class SagaPaymentResponseListener {
    private static final Logger LOG = LoggerFactory.getLogger(SagaPaymentResponseListener.class);
    private LocalDateTimeUtil localDateTimeUtil = new LocalDateTimeUtil();
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private OrderSagaService sagaService;

   /*
    Step 2 saga: get messaagee from payment service . Update order status in order db
    */
    @KafkaListener(topics = "t.saga.order.outbox.payment-validate.response", containerFactory = "stringDeserializerContainerFactory")
    public void onPaymentDataValidated(@Header(name = KafkaHeaders.RECEIVED_TIMESTAMP) long timestamp,
                                       @Payload String message) throws JsonMappingException, JsonProcessingException {
        var outboxMessage = objectMapper.readValue(message, OutboxMessage.class);
        LOG.debug("Step 2 Saga: get message from payment-service");
        if (StringUtils.equalsAny(outboxMessage.getPayload().getEventType(), OutboxEventType.VALIDATED_PAYMENT)
         && outboxMessage.getPayload().getSagaStatus() == SagaStatus.PROCESSING) {
            var paymentValidatedMessage = objectMapper.readValue(outboxMessage.getPayload().getPayload(),
                    PaymentValidatedMessage.class);
            sagaService.updateOrderStatusToPaid(paymentValidatedMessage);
            LOG.debug("updating order id:" + paymentValidatedMessage.getPaymentId() + " status to PAID");
            LOG.debug("sending message to subscription-service");
        }
        else if (StringUtils.equalsAny(outboxMessage.getPayload().getEventType(), OutboxEventType.CANCELLED_PAYMENT)
                && outboxMessage.getPayload().getSagaStatus() == SagaStatus.PROCESSING){
            var paymentCanceledMessage = objectMapper.readValue(outboxMessage.getPayload().getPayload(),
                    PaymentCanceledMessage.class);
            sagaService.updateOrderStatusToCancelled(paymentCanceledMessage);
            LOG.debug("updating order id: " + paymentCanceledMessage.getOrderId() + " status to CANCELLED");
            LOG.debug("end ProcessOrderSaga");
        }
    }


}
