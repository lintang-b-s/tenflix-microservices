package com.lintang.netflik.orderservice.command.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lintang.netflik.orderservice.broker.listener.SagaPaymentResponseListener;
import com.lintang.netflik.orderservice.broker.message.*;
import com.lintang.netflik.orderservice.command.action.OrderOutboxAction;
import com.lintang.netflik.orderservice.command.action.OrderSagaAction;
import com.lintang.netflik.orderservice.entity.*;
import com.lintang.netflik.orderservice.util.OrderMessageMapper;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderSagaService {
    private static final Logger LOG = LoggerFactory.getLogger(OrderSagaService.class);


    @Autowired
    private OrderSagaAction orderSagaAction;
    @Autowired
    private OrderOutboxAction outboxAction;

    @Autowired
    private OrderMessageMapper orderMessageMapper;


/*
    Step 2 saga: get messaagee from order service . Update order status in order db
    */

    @Transactional
    public void updateOrderStatusToPaid(PaymentValidatedMessage paymentValidatedMessage) throws JsonProcessingException {
        String orderId = paymentValidatedMessage.getOrderId();

            OrderEntity order = orderSagaAction.updateOrderStatusAction(orderId, paymentValidatedMessage.getOrderStatus());
//            OrderPlanEntity orderPlanEntity  =orderSagaAction.getPlan(order.getId().toString());
            AddSubscriptionMessage subscriptionMessage = AddSubscriptionMessage.builder()
                    .orderId(order.getId().toString())
                    .userId(order.getUserId().toString())
                    .price(order.getPrice())
                    .orderStatus(order.getOrderStatus())
                    .failureMessages(order.getFailureMessages())
                    .paymentId(order.getPaymentId())
                    .planId(order.getPlan().getPlanId())
                    .build();
            var orderOutbox = outboxAction.insertOutbox(
                    "subscription.request",
                    orderId,
                    OutboxEventType.ADD_SUBSCRIPTION, subscriptionMessage, SagaStatus.PROCESSING
            );
            outboxAction.deleteOutbox(orderOutbox);
            LOG.debug("Step 2 saga: get messaagee from order service . Update order status in order db");
    }

    @Transactional
    public void updateOrderStatusToCancelled(PaymentCanceledMessage paymentCanceledMessage) throws JsonProcessingException {
        String orderId = paymentCanceledMessage.getOrderId();
        OrderEntity order = orderSagaAction.updateOrderStatusAction(orderId, paymentCanceledMessage.getOrderStatus());
        // end ProcessOrderSaga
    }

    @Transactional
    public void compensatingOrder(CompensatingOrderSubscriptionMessage addSubscriptionErrorMessage) {
        String orderId = addSubscriptionErrorMessage.getOrder().getId().toString();
        orderSagaAction.updateOrderStatusAction(orderId, OrderStatus.PENDING);
    }

    @Transactional
    public void sendMessageToOrderRequestTopic(AddedSubscriptionMessage addedSubscriptionMessage) throws JsonProcessingException{
        String orderId = addedSubscriptionMessage.getOrderId();
        OrderStatus orderStatus = OrderStatus.COMPLETED;
        OrderEntity order = orderSagaAction.findById(orderId);
        OrderMessage orderMessage = orderMessageMapper.orderEntityToOrderMessage(order);
        CompleteOrderMessage completeOrderMessage = CompleteOrderMessage.builder()
                .order(orderMessage).build(); // error OrderEntity.plan && OrderPlanEntity.order Stackoverflow ???
        var orderOutbox = outboxAction.insertOutbox( // error disini stackoverflow??
                "order.request",
                orderId,
                OutboxEventType.COMPLETE_ORDER, completeOrderMessage, SagaStatus.SUCCEEDED
        );  // salah disini
        outboxAction.deleteOutbox(orderOutbox);
    }

    @Transactional
    public void updateOrderStatusToCompleted(CompleteOrderMessage completeOrderMessage)
            throws JsonProcessingException {
        String orderId = completeOrderMessage.getOrder().getId().toString();
        OrderStatus orderStatus = completeOrderMessage.getOrder().getOrderStatus();
        String userId = completeOrderMessage.getOrder().getUserId().toString();
        OrderMessage order = completeOrderMessage.getOrder();


        orderSagaAction.updateOrderStatusAction(orderId, OrderStatus.COMPLETED);
    }

    @Transactional
    public void compensatingOrderAndPayment(AddSubscriptionErrorMessage addSubscriptionErrorMessage) throws JsonProcessingException {
        OrderEntity order = orderSagaAction.findById(addSubscriptionErrorMessage.getOrderId());
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        OrderMessage orderMessage =  modelMapper.map(order, OrderMessage.class);
        CompensatingOrderSubscriptionMessage compensatingMessage =
                CompensatingOrderSubscriptionMessage.builder()
                        .order(orderMessage).build();

        var compensatingPaymentOutbox = outboxAction.insertOutbox(
                "payment-validate.request",
                order.getId().toString(),
                OutboxEventType.COMPENSATING_ORDER_SUBSCRIPTION_ERROR,
                compensatingMessage, SagaStatus.COMPENSATING
        );
        outboxAction.deleteOutbox(compensatingPaymentOutbox);
        var compensatingOrderOutbox = outboxAction.insertOutbox(
                "order.request",
                order.getId().toString(),
                OutboxEventType.COMPENSATING_ORDER_SUBSCRIPTION_ERROR,
                compensatingMessage, SagaStatus.COMPENSATING
        );
        outboxAction.deleteOutbox(compensatingOrderOutbox);

    }
}
