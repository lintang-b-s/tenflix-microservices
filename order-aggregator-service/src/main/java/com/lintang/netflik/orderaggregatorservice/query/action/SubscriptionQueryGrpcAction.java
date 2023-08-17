package com.lintang.netflik.orderaggregatorservice.query.action;


import com.lintang.netflik.models.*;
import com.lintang.netflik.orderaggregatorservice.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubscriptionQueryGrpcAction {
    @GrpcClient("subscription-service")
    private SubscriptionServiceGrpc.SubscriptionServiceBlockingStub subscriptionStub;

    public GetUserCurrentSubscriptionResponse getUserCurrentSubscription(String userId ){
        GetUserCurrentSubscriptionRequest getUserCurrentSubscriptionRequest=
                GetUserCurrentSubscriptionRequest.newBuilder()
                        .setGetSubscriptionRequestDto(GetActiveSubscriptionDto.newBuilder()
                                .setUserId(userId)
                                .build())
                        .build();
        GetUserCurrentSubscriptionResponse getUserCurrentSubscriptionResponse = null;
        try{
            getUserCurrentSubscriptionResponse = this.subscriptionStub.getUserCurrentSubscription(getUserCurrentSubscriptionRequest);
        }catch (RuntimeException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }

        return getUserCurrentSubscriptionResponse;
    }
}
