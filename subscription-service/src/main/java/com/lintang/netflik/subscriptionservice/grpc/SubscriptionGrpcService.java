package com.lintang.netflik.subscriptionservice.grpc;


import com.google.protobuf.Empty;
import com.lintang.netflik.models.*;
import com.lintang.netflik.subscriptionservice.command.action.SubscriptionAction;
import com.lintang.netflik.subscriptionservice.entity.PlanEntity;
import com.lintang.netflik.subscriptionservice.entity.SubscriptionEntity;
import com.lintang.netflik.subscriptionservice.query.action.SubscriptionGrpcAction;
import com.lintang.netflik.subscriptionservice.util.SubscriptionMapper;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@GrpcService
public class SubscriptionGrpcService extends SubscriptionServiceGrpc.SubscriptionServiceImplBase {
    @Autowired
    private SubscriptionGrpcAction subscriptionGrpcAction;

    @Autowired
    private SubscriptionAction action;

    @Autowired
    private SubscriptionMapper subscriptionMapper;

 /*
    Getplan detail

    */

    @Override
    public void getPlan(GetPlanGrpcRequest getPlanGrpcRequest, StreamObserver<GetPlanGrpcResponse> responseObserver) {
        Optional<PlanEntity> plan = subscriptionGrpcAction.getPlanFromDb(getPlanGrpcRequest);
        if (!plan.isPresent()) {
            responseObserver.onError(Status.NOT_FOUND.withDescription("Plan with id "+ getPlanGrpcRequest.getCreateOrder().getPlanId()  +
                    " Not found").asRuntimeException());
            return;
        }
        PlanEntity planDb = plan.get();
        PlanDto planDto = PlanDto.newBuilder()
                .setPlanId(planDb.getPlanId()).setName(planDb.getName()).setDescription(planDb.getDescription())
                .setActivePeriod(planDb.getActivePeriod()).setPrice( planDb.getPrice().longValue()).setActivePeriod(planDb.getActivePeriod())
                .build();
        responseObserver.onNext(GetPlanGrpcResponse.newBuilder().setPlan(planDto).build());
        responseObserver.onCompleted();
    }


 /*
    create plan subscription

    */
    @Override
    public void createPlan(CreatePlanGrpcRequest request,
                           StreamObserver<CreatePlanGrpcResponse> responseObserver) {
        CreatePlanDto createPlanDto = request.getCreatePlanDto();
        PlanEntity plan = action.createPlan(createPlanDto);

        PlanDto planDto =PlanDto.newBuilder()
                .setPlanId(plan.getPlanId()).setName(plan.getName())
                .setPrice(plan.getPrice().longValue()).setDescription(plan.getDescription())
                .setActivePeriod(plan.getActivePeriod()).setDescription(plan.getDescription())
                .build();


        responseObserver.onNext(CreatePlanGrpcResponse.newBuilder().setPlanDto(planDto).build());
        responseObserver.onCompleted();
    }

 /*
  Buat check pas createOrder . kalau ada subscription yang aktif pas create order , throw badrequesterror

    */
    @Override
    public void getActiveSubscription(GetActiveSubscriptionRequest request,
                                      StreamObserver<Empty> responseObserver) {
        List<SubscriptionEntity> subscriptionEntities = subscriptionGrpcAction.getActiveUserSubscription(request.getGetActiveSubscriptionDto()
                .getUserId());

        if (subscriptionEntities.size() != 0) {
            responseObserver.onError(Status.FAILED_PRECONDITION.withDescription("User with id " + request.getGetActiveSubscriptionDto().getUserId()
            + " have active subscription! ").asRuntimeException());
            return;
        }

        responseObserver.onNext(Empty.newBuilder().build());
        responseObserver.onCompleted();
    }

/*
    Buat check subscription yang lagi aktif

    */
    @Override
    public void getUserCurrentSubscription(GetUserCurrentSubscriptionRequest request,
                                           StreamObserver<GetUserCurrentSubscriptionResponse> responseObserver) {
        List<SubscriptionEntity> subscriptionEntities = subscriptionGrpcAction.getActiveUserSubscription(request.getGetSubscriptionRequestDto()
                .getUserId());
        SubscriptionEntity subscriptionEntity = subscriptionEntities.get(0);
         if (subscriptionEntities.size() == 0) {
            responseObserver.onError(Status.FAILED_PRECONDITION.withDescription("User with id " + request.getGetSubscriptionRequestDto().getUserId()
                    + " doesnt have active subscription! ").asRuntimeException());
            return;
        }
         SubscriptionDto subscriptionDto = this.subscriptionMapper.subscriptionEntityToProtoDto(subscriptionEntity);
        GetUserCurrentSubscriptionResponse response = GetUserCurrentSubscriptionResponse.newBuilder()
                .setSubscriptionDto(subscriptionDto)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getUserSubscriptionByOrderId(GetUserSubscriptionByOrderIdRequest request,
                                             StreamObserver<GetUserSubscriptionByOrderIdResponse> responseObserver) {
        Optional<SubscriptionEntity> subscriptionEntity = subscriptionGrpcAction.getSubcriptionByOrderId(request.getOrderId());

        if (!subscriptionEntity.isPresent()) {
            responseObserver.onError(Status.NOT_FOUND.withDescription("subscription with ordeerId " + request.getOrderId() + " not found").asRuntimeException());
        }
        GetUserSubscriptionByOrderIdResponse response=
                GetUserSubscriptionByOrderIdResponse.newBuilder().setSubscriptionDto(subscriptionMapper.
                        subscriptionEntityToProtoDto(subscriptionEntity.get())).build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }


}
