package com.lintang.netflik.orderservice.command.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.protobuf.ByteString;
import com.google.protobuf.Empty;
import com.lintang.netflik.models.*;
import com.lintang.netflik.orderservice.*;
import com.lintang.netflik.orderservice.command.action.OrderAction;
import com.lintang.netflik.orderservice.command.action.OrderOutboxAction;
import com.lintang.netflik.orderservice.entity.OrderEntity;
import com.lintang.netflik.orderservice.entity.OrderStatus;
import com.lintang.netflik.orderservice.entity.OutboxEventType;
import com.lintang.netflik.orderservice.entity.SagaStatus;
import com.lintang.netflik.orderservice.exception.BadRequestException;
import com.lintang.netflik.orderservice.exception.ResourceNotFoundException;
import com.lintang.netflik.orderservice.repository.OrderJpaRepository;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
//import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.lintang.netflik.models.OrderServiceGrpc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@GrpcService
public class OrderGrpcService extends OrderServiceGrpc.OrderServiceImplBase {
    private static final Logger LOG = LoggerFactory.getLogger(OrderGrpcService.class);

    @Autowired
    private OrderJpaRepository orderJpaRepository;

    @Autowired
    private OrderAction orderAction;

    @Autowired
    private OrderOutboxAction orderOutboxAction;

    @Override
    public void createOrder(CreateOrderGrpcRequest request, StreamObserver<CreateOrderGrpcResponse> responseObserver) {
        CreateOrderGrpcResponse.Builder builder = CreateOrderGrpcResponse.newBuilder();
        OrderDto orderDto = request.getOrder();

        boolean isExists = orderAction.userHasMoreThanOneOrder(orderDto.getUserId());
        if (isExists) {
            responseObserver.onError(Status.FAILED_PRECONDITION.withDescription("User with id "+ request.getOrder().getUserId()
                            + " has more than 1 PENDING order!")
                    .asRuntimeException());
            return;
        }

        OrderDto savedOrder = orderAction.saveOrder(orderDto);
        builder.setCreatedOrder(savedOrder).build();
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }


    /*
    * Step 1 Saga Process Order:
    *  send mapMidtransPayment (Map<String,Any (byteString)>) to topic "t.saga.order.outbox.payment-validate.request"
    * */
    @Override
    public void processOrderSaga(ProcessOrderRequest request, StreamObserver<Empty> responseObserver) {

        String orderId = (String) SerializationUtils.deserialize(request.getPaymentNotification().getNotificationResMap().get("order_id").getValue().toByteArray());
        Optional<OrderEntity> orderEntityOptional = orderAction.findOrderById(orderId);
        if (!orderEntityOptional.isPresent()){
            responseObserver.onError(new ResourceNotFoundException("order with id " + orderId + " not found"));
        }

//        Map<String, Any> map
        var mapNotificationMidtrans = request.getPaymentNotification().getNotificationResMap(); // salah disini
        try {
            var paymentOutbox = orderOutboxAction.insertOutbox(
                    "payment-validate.request",
                    request.getPaymentNotification().getNotificationResMap().get("order_id").toString(),
                    OutboxEventType.VALIDATE_PAYMENT, mapNotificationMidtrans, SagaStatus.STARTED
            );
            LOG.debug("Step 1 Saga:  Send MapNotifMidtrans to Payment-Service !");
            orderOutboxAction.deleteOutbox(paymentOutbox);
        } catch (JsonProcessingException e) {
            responseObserver.onError(new BadRequestException("Json processing error")); // error disini
            LOG.debug("erorr in step 1 saga: proccessOrderSaga insertoutbox");
        }

        responseObserver.onNext(Empty.newBuilder().build());
        responseObserver.onCompleted();
    }

}
