package com.lintang.netflik.orderservice.grpc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.protobuf.Empty;
import com.lintang.netflik.models.*;
import com.lintang.netflik.orderservice.command.action.OrderAction;
import com.lintang.netflik.orderservice.command.action.OrderOutboxAction;
import com.lintang.netflik.orderservice.command.action.OrderSagaAction;
import com.lintang.netflik.orderservice.command.action.ProcessOrderAction;
import com.lintang.netflik.orderservice.command.request.PaymentEntity;
import com.lintang.netflik.orderservice.entity.OrderEntity;
import com.lintang.netflik.orderservice.entity.OutboxEventType;
import com.lintang.netflik.orderservice.entity.SagaStatus;
import com.lintang.netflik.orderservice.exception.BadRequestException;
import com.lintang.netflik.orderservice.exception.ResourceNotFoundException;
import com.lintang.netflik.orderservice.repository.OrderJpaRepository;
import com.lintang.netflik.orderservice.util.OrderMapper;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
//import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.lintang.netflik.models.OrderServiceGrpc;

import java.util.List;
import java.util.Objects;
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

    @Autowired
    private ProcessOrderAction processOrderAction;

    @Autowired
    private OrderSagaAction orderSagaAction;

    @Autowired
    private OrderMapper orderMapper;

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

        String orderId = request.getPaymentNotification().getNotificationResMap().get("order_id").toString();
        Optional<OrderEntity> orderEntityOptional = orderAction.findOrderById(orderId);
        if (!orderEntityOptional.isPresent()){
            responseObserver.onError(Status.NOT_FOUND.withDescription("Order with id "+orderId
                    + " Not found!").asRuntimeException());
        }

//        Map<String, Any> map
        PaymentEntity mapNotificationMidtrans = processOrderAction.convertMaptoHashMap(request.getPaymentNotification().getNotificationResMap(), request.getPaymentNotification().getBank(),
                request.getPaymentNotification().getVaNum());

        try {
            var paymentOutbox = orderOutboxAction.insertOutbox(
                    "payment-validate.request",
                    orderId,
                    OutboxEventType.VALIDATE_PAYMENT, mapNotificationMidtrans, SagaStatus.STARTED
            );
            LOG.debug("Step 1 Saga:  Send MapNotifMidtrans to Payment-Service !");
            orderOutboxAction.deleteOutbox(paymentOutbox);
        } catch (JsonProcessingException e) {
            responseObserver.onError(Status.INVALID_ARGUMENT.withDescription("Json processing error").asRuntimeException());
            LOG.debug("erorr in step 1 saga: proccessOrderSaga insertoutbox");
        }

        responseObserver.onNext(Empty.newBuilder().build());
        responseObserver.onCompleted();
    }

/*
    Get Order Detail
    */
    @Override
    public void getUserOrderDetail(GetUserOrderDetailRequest request, StreamObserver<GetUserOrderDetailResponse> responseObserver) {
        String userId = request.getUserId();
        String orderId = request.getOrderId();
        GetUserOrderDetailResponse.Builder builder = GetUserOrderDetailResponse.newBuilder();

        Optional<OrderEntity> orderEntity = orderAction.findOrderById(orderId);
        if (!orderEntity.isPresent()) {
            responseObserver.onError(Status.NOT_FOUND.withDescription("Order with id "+orderId
                    + " Not found!").asRuntimeException());
            return;
        }

        if (!StringUtils.equals(orderEntity.get().getUserId().toString(), userId)) {
            responseObserver.onError(Status.FAILED_PRECONDITION.withDescription("You are not authorized to see order " +orderId).asRuntimeException());
            return;
        }

        OrderEntity orderDb = orderEntity.get();
        builder.setOrderDto(orderMapper.orderEntityToDto(orderDb, orderDb.getPlan())).build();
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();

    }


    @Override
    public void getUserOrderHistory(GetUserOrderHistoryRequest request,
                                    StreamObserver<GetUserOrderHistoryResponse> responseObserver){
        GetUserOrderHistoryResponse.Builder builder = GetUserOrderHistoryResponse.newBuilder();
        List<OrderEntity> orders = orderAction.findByUserId(request.getUserId());

        if (orders.size() == 0 ) {
            OrderDto orderDto = OrderDto.newBuilder()
                    .setId("null").setUserId(request.getUserId())
                    .setPrice(0)
                    .build();
            responseObserver.onNext(builder.setOrderDto(orderDto).build());
            responseObserver.onCompleted();
            return;
        }

        for (OrderEntity order: orders) {
            builder.setOrderDto(orderMapper.orderEntityToDto(order, order.getPlan())).build();
            responseObserver.onNext(builder.build());
        }
        responseObserver.onCompleted();
    }

}
