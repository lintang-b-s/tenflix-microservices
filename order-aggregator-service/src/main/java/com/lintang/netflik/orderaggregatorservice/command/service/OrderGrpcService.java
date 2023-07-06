package com.lintang.netflik.orderaggregatorservice.command.service;


import com.google.protobuf.Any;
import com.google.protobuf.ByteString;
import com.lintang.netflik.models.*;
import com.lintang.netflik.orderaggregatorservice.command.action.OrderGrpcAction;
import com.lintang.netflik.orderaggregatorservice.command.request.CreateOrderRequest;
import com.lintang.netflik.orderaggregatorservice.command.response.CreateOrderResponse;
import com.lintang.netflik.orderaggregatorservice.command.response.KeycloakUserDto;
import com.lintang.netflik.orderaggregatorservice.command.response.OrderDtoResponse;
import com.lintang.netflik.orderaggregatorservice.common.OrderStatus;
import com.lintang.netflik.orderaggregatorservice.entity.OrderEntity;
import com.lintang.netflik.orderaggregatorservice.entity.OrderPlanEntity;
import com.lintang.netflik.orderaggregatorservice.entity.PlanEntity;
import com.lintang.netflik.orderaggregatorservice.entity.UserEntity;
import com.lintang.netflik.orderaggregatorservice.exception.BadRequestException;
import com.lintang.netflik.orderaggregatorservice.exception.ResourceNotFoundException;
import com.lintang.netflik.orderaggregatorservice.util.Mapper;
import com.lintang.netflik.orderaggregatorservice.util.PlanMapper;
import com.midtrans.httpclient.error.MidtransError;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.SerializationUtils;
import org.springframework.web.client.RestTemplate;


import java.math.BigDecimal;
import java.security.Key;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderGrpcService {





    @Autowired
    private Mapper mapper;

    @Autowired
    private  OrderGrpcAction orderGrpcAction;
    @Autowired
    private PlanMapper planMapper;
    public CreateOrderResponse createOrder(CreateOrderRequest createOrderRequest, String userId) throws MidtransError {
        GetPlanGrpcResponse getPlanGrpcResponse =  orderGrpcAction.getPlanFromSubscriptionService(createOrderRequest);
        PlanDto planDto = getPlanGrpcResponse.getPlan();
        PlanEntity plan = planMapper.planDtoToPlanEntity(planDto);

//        get user detail from keycloak
        KeycloakUserDto keycloakUserDto = orderGrpcAction.getUserDetail(userId);

//        create order
        OrderDtoResponse orderDto =orderGrpcAction.createOrderFromOrderService(createOrderRequest, planDto);

//        get redirect url
        GetRedirectUrl getRedirectUrl = orderGrpcAction.getRedirectUrl(orderDto, keycloakUserDto, planDto);

        return CreateOrderResponse.builder().order(orderDto).orderStatus(OrderStatus.PENDING.name()).redirectUrl(getRedirectUrl.getRedirectUrl())
                .build();
    }

    public String processOrder(Map<String, Object> notificationRes) {

       orderGrpcAction.proccessOrder(notificationRes);
        return "Transaction Processed";
    }

}
