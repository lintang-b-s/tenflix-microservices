package com.lintang.netflik.paymentservice.broker.listener.saga;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.Any;
import com.lintang.netflik.models.PaymentNotification;
import com.lintang.netflik.paymentservice.broker.message.CompensatingOrderSubscriptionMessage;
import com.lintang.netflik.paymentservice.broker.message.OutboxMessage;
import com.lintang.netflik.paymentservice.command.service.PaymentSagaService;
import com.lintang.netflik.paymentservice.entity.OutboxEventType;
import com.lintang.netflik.paymentservice.entity.SagaStatus;
import com.lintang.netflik.paymentservice.repository.OutboxRepository;
import com.lintang.netflik.paymentservice.util.LocalDateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.apache.commons.lang3.StringUtils;
import java.util.Map;


@Component
public class SagaPaymentRequestListener {
    private static final Logger LOG = LoggerFactory.getLogger(SagaPaymentRequestListener.class);
//            t.saga.order.outbox.payment-validate.request

    private LocalDateTimeUtil localDateTimeUtil = new LocalDateTimeUtil();
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PaymentSagaService paymentSagaService;

    @Autowired
    private OutboxRepository outboxRepository;


   /*
    Step 1 Saga process Order: check transactionstatus payment midtrans & send orderStatus to orders-service
    */

    @KafkaListener(topics = "t.saga.order.outbox.payment-validate.request", containerFactory = "stringDeserializerContainerFactory")
    public void onValidatePayment(@Header(name = KafkaHeaders.RECEIVED_TIMESTAMP) long timestamp,
                                  @Payload String message) throws JsonMappingException, JsonProcessingException {
        var outboxMessage = objectMapper.readValue(message, OutboxMessage.class);
        LOG.debug("step 1 Saga: get message from order service!");
        if (StringUtils.equalsAny(outboxMessage.getPayload().getEventType(), OutboxEventType.VALIDATE_PAYMENT) &&
                (outboxMessage.getPayload().getSagaStatus() == SagaStatus.STARTED)) {

            var mapMessageMidtrans = objectMapper.readValue(outboxMessage.getPayload().getPayload(),
                    PaymentNotification.class);
            Map<String, Any> mapNotificationMidtrans = mapMessageMidtrans.getNotificationResMap();
            paymentSagaService.validatePayment(mapNotificationMidtrans);
            LOG.debug("send message to order service . Payment validated! ");
        }
        else if (StringUtils.equalsAny(outboxMessage.getPayload().getEventType(), OutboxEventType.COMPENSATING_ORDER_SUBSCRIPTION_ERROR)
         && (outboxMessage.getPayload().getSagaStatus() == SagaStatus.COMPENSATING)) {
            var compensatingOrderSubscriptionMessage = objectMapper.readValue(
                    outboxMessage.getPayload().getPayload(), CompensatingOrderSubscriptionMessage.class
            );

            paymentSagaService.compensatingPaymentTransaction(compensatingOrderSubscriptionMessage);

            LOG.debug("Compensating transaction. Delete payment data in database && refund payment to user ");
        }
    }


}
