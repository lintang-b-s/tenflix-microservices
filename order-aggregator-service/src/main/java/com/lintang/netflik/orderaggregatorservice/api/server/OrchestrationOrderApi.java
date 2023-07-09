package com.lintang.netflik.orderaggregatorservice.api.server;

import com.lintang.netflik.orderaggregatorservice.command.request.CreateOrderRequest;
import com.lintang.netflik.orderaggregatorservice.command.response.CreateOrderResponse;
import com.lintang.netflik.orderaggregatorservice.command.service.OrderGrpcService;
import com.midtrans.httpclient.error.MidtransError;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/api/v1/orders")
@AllArgsConstructor
public class OrchestrationOrderApi {

    @Autowired
    private OrderGrpcService orderGrpcService;


    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_user', 'ROLE_ADMIN')")
    public ResponseEntity<CreateOrderResponse> createOrder(
            @RequestBody(required = true) CreateOrderRequest requestBody, @AuthenticationPrincipal Jwt principal) throws MidtransError {
        CreateOrderResponse responseBody = orderGrpcService.createOrder(requestBody, principal.getSubject());


        return ResponseEntity.ok().body(responseBody);
    }


    @PostMapping("/notificationMidtrans")
    public ResponseEntity<String> processOrder(
            @RequestBody Map<String, Object> notificationRes
    ) throws MidtransError {
        String response=  orderGrpcService.processOrder(notificationRes);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
