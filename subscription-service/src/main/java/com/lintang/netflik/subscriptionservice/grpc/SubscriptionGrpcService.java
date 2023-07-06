package com.lintang.netflik.subscriptionservice.grpc;


import com.lintang.netflik.models.*;
import com.lintang.netflik.subscriptionservice.command.action.SubscriptionAction;
import com.lintang.netflik.subscriptionservice.entity.PlanEntity;
import com.lintang.netflik.subscriptionservice.query.action.SubscriptionGrpcAction;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@GrpcService
public class SubscriptionGrpcService extends SubscriptionServiceGrpc.SubscriptionServiceImplBase {
    @Autowired
    private SubscriptionGrpcAction subscriptionGrpcAction;

    @Autowired
    private SubscriptionAction action;

    @Override
    public void getPlan(GetPlanGrpcRequest getPlanGrpcRequest, StreamObserver<GetPlanGrpcResponse> responseObserver) {
        Optional<PlanEntity> plan = subscriptionGrpcAction.getPlanFromDb(getPlanGrpcRequest);
        if (!plan.isPresent()) {
            responseObserver.onError(Status.NOT_FOUND.withDescription("Plan with id "+ getPlanGrpcRequest.getCreateOrder().getPlanId()  +
                    " Not found").asRuntimeException());
        }
        PlanEntity planDb = plan.get();
        PlanDto planDto = PlanDto.newBuilder()
                .setPlanId(planDb.getPlanId()).setName(planDb.getName()).setDescription(planDb.getDescription())
                .setActivePeriod(planDb.getActivePeriod()).setPrice( planDb.getPrice().longValue()).setActivePeriod(planDb.getActivePeriod())
                .build();
        responseObserver.onNext(GetPlanGrpcResponse.newBuilder().setPlan(planDto).build());
        responseObserver.onCompleted();
    }



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
}
