package com.lintang.netflik.orderaggregatorservice.api.server;

import com.lintang.netflik.orderaggregatorservice.command.request.CreatePlanRequest;
import com.lintang.netflik.orderaggregatorservice.command.response.CreatePlanResponse;
import com.lintang.netflik.orderaggregatorservice.command.service.SubscriptionAggregateGrpcService;
import com.lintang.netflik.orderaggregatorservice.query.response.CheckSubscriptionResponse;
import com.lintang.netflik.orderaggregatorservice.query.service.SubscriptionQueryGrpcService;
import lombok.AllArgsConstructor;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/v1/subscription")
@AllArgsConstructor
public class SubscriptionApi {

    @Autowired
    private SubscriptionAggregateGrpcService subscriptionAggregateGrpcService;

    @Autowired
    private SubscriptionQueryGrpcService subscriptionQueryGrpcService;

//    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
    public ResponseEntity<CreatePlanResponse> createPlan(
            @RequestBody CreatePlanRequest request
    ) {
        CreatePlanResponse response = subscriptionAggregateGrpcService.createPlan(request);
        return ok(response);
    }

    @GetMapping(path = "/check",produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
    public ResponseEntity<CheckSubscriptionResponse> checkUserSubscription(@AuthenticationPrincipal Jwt principal) {
        CheckSubscriptionResponse checkSubscriptionResponse = this.subscriptionQueryGrpcService.checkUserSubscription(principal.getSubject());

        return ok(checkSubscriptionResponse);
    }

    @GetMapping(value = "tes", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CreatePlanResponse> tes() {
        CreatePlanResponse createPlanResponse = CreatePlanResponse.builder()
                .planId(1)
                .price(100).description("tesss")
                .build();
        return ResponseEntity.ok().body(createPlanResponse);
    }


}
