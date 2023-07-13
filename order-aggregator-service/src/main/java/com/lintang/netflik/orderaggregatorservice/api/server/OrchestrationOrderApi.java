package com.lintang.netflik.orderaggregatorservice.api.server;

import com.lintang.netflik.orderaggregatorservice.command.request.CreateOrderRequest;
import com.lintang.netflik.orderaggregatorservice.command.response.CreateOrderResponse;
import com.lintang.netflik.orderaggregatorservice.command.service.OrderGrpcService;
import com.lintang.netflik.orderaggregatorservice.query.response.GetOrderDetailResponse;
import com.lintang.netflik.orderaggregatorservice.query.response.GetOrdersResponse;
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

import static org.springframework.http.ResponseEntity.ok;


@RestController
@RequestMapping("/api/v1/orders")
@AllArgsConstructor
public class OrchestrationOrderApi {

    @Autowired
    private OrderGrpcService orderGrpcService;

/*
    create user order
*/

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_user', 'ROLE_ADMIN')")
    public ResponseEntity<CreateOrderResponse> createOrder(
            @RequestBody(required = true) CreateOrderRequest requestBody, @AuthenticationPrincipal Jwt principal) throws MidtransError {
        CreateOrderResponse responseBody = orderGrpcService.createOrder(requestBody, principal.getSubject());


        return ok().body(responseBody);
    }


    /*
    Process order setelah dapet notifikasi dari midtrans
    pake transactional Saga + outbox pattern
   */

    @PostMapping("/notificationMidtrans")
    public ResponseEntity<String> processOrder(
            @RequestBody Map<String, Object> notificationRes
    ) throws MidtransError {
        String response=  orderGrpcService.processOrder(notificationRes);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    /*
        Get user order Detail with payment detail , subscription detail from 3 service
    */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_user', 'ROLE_ADMIN')")
    public ResponseEntity<GetOrderDetailResponse> getOrderDetail(
            @PathVariable("id") String orderId, @AuthenticationPrincipal Jwt principal
    ) {
        GetOrderDetailResponse response = orderGrpcService.getOrderDetail(orderId, principal.getSubject());
        return ok(response);
    }

/*
    Get user oder history
    */
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_user', 'ROLE_ADMIN')")
    public ResponseEntity<GetOrdersResponse> getOrderHistory(
            @AuthenticationPrincipal Jwt principal
    ) {
        GetOrdersResponse response = orderGrpcService.getOrderHistory(principal.getSubject());
        return ok(response);
    }
}
