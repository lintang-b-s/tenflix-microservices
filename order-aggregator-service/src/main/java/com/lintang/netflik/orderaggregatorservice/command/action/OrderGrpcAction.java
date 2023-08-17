package com.lintang.netflik.orderaggregatorservice.command.action;


import com.google.protobuf.Any;
import com.google.protobuf.ByteString;
import com.google.protobuf.Empty;
import com.lintang.netflik.models.*;
import com.lintang.netflik.orderaggregatorservice.command.request.CreateOrderRequest;
import com.lintang.netflik.orderaggregatorservice.command.response.CreateOrderResponse;
import com.lintang.netflik.orderaggregatorservice.command.response.KeycloakUserDto;
import com.lintang.netflik.orderaggregatorservice.command.response.OrderDtoResponse;
import com.lintang.netflik.orderaggregatorservice.command.service.OrderGrpcService;
import com.lintang.netflik.orderaggregatorservice.entity.OrderEntity;
import com.lintang.netflik.orderaggregatorservice.entity.OrderPlanEntity;
import com.lintang.netflik.orderaggregatorservice.exception.BadRequestException;
import com.lintang.netflik.orderaggregatorservice.exception.ResourceNotFoundException;
import com.lintang.netflik.orderaggregatorservice.query.response.GetOrderDetailResponse;
import com.lintang.netflik.orderaggregatorservice.query.response.GetOrdersResponse;
import com.lintang.netflik.orderaggregatorservice.util.Mapper;
import com.lintang.netflik.orderaggregatorservice.util.OrderMapper;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.SerializationUtils;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Component
@RequiredArgsConstructor
public class OrderGrpcAction {

    private static final Logger LOG = LoggerFactory.getLogger(OrderGrpcService.class);
    @Value("keycloak.admin.username")
    private String username;
    @Value("keycloak.admin.password")
    private String password;

    private final RestTemplateBuilder restTemplateBuilder;


    @GrpcClient("subscription-service")
    private SubscriptionServiceGrpc.SubscriptionServiceBlockingStub subscriptionStub;

    @GrpcClient("user-service")
    private UserServiceGrpc.UserServiceBlockingStub userStub;

    @GrpcClient("order-service")
    private OrderServiceGrpc.OrderServiceBlockingStub orderStub;
    @GrpcClient("payment-service")
    private PaymentServiceGrpc.PaymentServiceBlockingStub paymentStub;


    @Autowired
    private Mapper mapper;
    @Autowired
    private OrderMapper orderMapper;

   /*
   * Get plan from subscription service
   * */

    public GetPlanGrpcResponse getPlanFromSubscriptionService(CreateOrderRequest createOrderRequest, String userId) {
        GetPlanGrpcRequest getPlanGrpcRequest = GetPlanGrpcRequest.newBuilder()
                .setCreateOrder(CreateOrder.newBuilder().setPlanId(createOrderRequest.getPlanId()).setUserId(userId).build()) //saalah disini
                .build();
        GetPlanGrpcResponse getPlanGrpcResponse = null;

        try{
            getPlanGrpcResponse = this.subscriptionStub.getPlan(getPlanGrpcRequest);
        } catch (RuntimeException e) {
            throw new ResourceNotFoundException(e.getMessage());

        }
        return getPlanGrpcResponse;
    }

/*

    Get user active subscription (cek jika user masih ada subcription active maka throw badrequesterrorr)

 */

    public void getActiveSubscriptionFromSubscriptionService(String userId) {
        GetActiveSubscriptionRequest getActiveSubscriptionRequest = GetActiveSubscriptionRequest.newBuilder()
                .setGetActiveSubscriptionDto(GetActiveSubscriptionDto.newBuilder().setUserId(userId).build())
                .build();
        try{
             this.subscriptionStub.getActiveSubscription(getActiveSubscriptionRequest);
        } catch (RuntimeException e) {
            throw new BadRequestException(e.getMessage());
        }
        return ;
    }


        /*
        * Get user detail from keycloak server
        * */
     public KeycloakUserDto getUserDetail(String userId)  {
         RestTemplate restTemplate =  new RestTemplate();


        Map token = getAccessToken(restTemplate).getBody();
        String accToken = (String) token.get("access_token");
        String url = "http://keycloak-tenflix:8080/admin/realms/tenflix/users/" + userId;

        ResponseEntity<Map> response = null;
        try{
            HttpHeaders headers = createHttpHeaders(accToken);
            HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
            response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class );


        }catch (Exception e) {
            throw new BadRequestException("error " + e.getMessage());
        }
        Map map = response.getBody();
        KeycloakUserDto keycloakUserDto = KeycloakUserDto.builder().email((String) map.get("email"))
                .username((String) map.get("username")).id((String) map.get("id")).firstName((String) map.get("firstName"))
                .lastName((String) map.get("lastName"))
                .build();

        return keycloakUserDto;

     }

     public OrderDtoResponse createOrderFromOrderService(CreateOrderRequest createOrderRequest,PlanDto planDto , String userId) {

         OrderPlanEntity orderPlanEntity = mapper.planDtoToOrderPlanEntity(planDto);

//    new order
         OrderEntity createdOrder = mapper.createOrderReqToOrderEntity(createOrderRequest, planDto, orderPlanEntity , userId);
         OrderPlanDto orderPlanDto = mapper.orderPlanEntityToOrderPlanDto(orderPlanEntity);

         CreateOrderGrpcRequest createOrderGrpcRequest = mapper.createdOrderToCreateOrderGpcRequest(
                     createdOrder, planDto, orderPlanDto
             );
            OrderDto createOrderGrpcResponse = null;
        try{
             createOrderGrpcResponse = this.orderStub.createOrder(createOrderGrpcRequest).getCreatedOrder();
        }catch (RuntimeException e) {
            throw  new BadRequestException(e.getMessage() );
        }

         OrderDtoResponse orderDtoResponse = orderMapper.orderDtoToOrderResponse(createOrderGrpcResponse);
        return orderDtoResponse;
     }

     public void proccessOrder(Map<String, Object>  notificationRes) {
         Map<String, Any> notificationProto = new HashMap<>();
         notificationRes.forEach(
                 (key, value)
                         -> {
                     // any = bytearray , harus diserialize
                     byte[] data = SerializationUtils.serialize(value);
                     notificationProto.put(key,Any.newBuilder().setValue(ByteString.copyFrom(data)).build() );
                 }
         );
         ProcessOrderRequest processOrderRequest = ProcessOrderRequest.newBuilder()
                 .setPaymentNotification(PaymentNotification.newBuilder().putAllNotificationRes(notificationProto).build())
                 .build();
         this.orderStub.processOrderSaga(processOrderRequest);
         return ;
     }



     public GetRedirectUrl getRedirectUrl (OrderDtoResponse orderDtoResponse, KeycloakUserDto customerDetails, PlanDto planDto) {
         TransactionDetails transactionDetails =  TransactionDetails.newBuilder()
                 .setOrderId(orderDtoResponse.getId().toString()).setGrossAmount(orderDtoResponse.getPrice().longValue())
                 .setCreditCard(true)
                 .setCustomerDetails(CustomerDetails.newBuilder().setId(customerDetails.getId())
                         .setAddress("indonesia").setEmail(customerDetails.getEmail()).setPhone("+62")
                         .setName(customerDetails.getFirstName()+ " " + customerDetails.getLastName()).build())
                 .setItemDetails(
                         ItemDetails.newBuilder().setPlanId(planDto.getPlanId()).setPrice(planDto.getPrice())
                                 .setName(planDto.getName()).build())
                 .build();
         PaymentGetRedirectUrlGrpcRequest paymentGetRedirectUrlGrpcRequest = PaymentGetRedirectUrlGrpcRequest.newBuilder()
                 .setTransactionDetail(transactionDetails)
                 .build();
         GetRedirectUrl getRedirectUrl = this.paymentStub.getPaymentRedirectUrl(paymentGetRedirectUrlGrpcRequest).getGetRedirectUrl();


        return getRedirectUrl;
     }

 /*
     get order detail from order-service
 */
    public GetUserOrderDetailResponse getUserOrderDetail(String orderId, String userId ) {
        GetUserOrderDetailRequest getUserOrderDetailRequest =
                GetUserOrderDetailRequest.newBuilder()
                        .setOrderId(orderId).setUserId(userId)
                        .build();

        GetUserOrderDetailResponse getOrderDetailResponse = null;
        try{
            getOrderDetailResponse= this.orderStub.getUserOrderDetail(getUserOrderDetailRequest);

        }catch (RuntimeException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }

        return getOrderDetailResponse;
    }

/*
    Get Payment Detail by order Id from payment-service

 */
    public GetPaymentByOrderIdResponse getPaymentDetailFromPaymentService(String orderId ){
        GetPaymentByOrderIdRequest getPaymentByOrderIdRequest=
                GetPaymentByOrderIdRequest.newBuilder().setOrderId(orderId)
                        .build();

        GetPaymentByOrderIdResponse getPaymentByOrderIdResponse= null;
        try{
            getPaymentByOrderIdResponse = this.paymentStub.getPaymentByOrderId(getPaymentByOrderIdRequest);

        } catch (RuntimeException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
        return getPaymentByOrderIdResponse;
    }

/*
        Get subcription Detail by order Id from subscription-service

    */

    public GetUserSubscriptionByOrderIdResponse getSubcriptionDetailFromSubscriptionService(String orderId,String userId ) {
        GetUserSubscriptionByOrderIdRequest request=
                GetUserSubscriptionByOrderIdRequest.newBuilder().setOrderId(orderId).setUserId(userId).build();
        GetUserSubscriptionByOrderIdResponse response= null;
        try{
            response = this.subscriptionStub.getUserSubscriptionByOrderId(request);
        }
        catch (RuntimeException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }

        return response;
    }


    public List<OrderDtoResponse> getUserOrderHistory(String userId ) {
        GetUserOrderHistoryRequest request=
                GetUserOrderHistoryRequest.newBuilder().setUserId(userId).build();
        Iterator<GetUserOrderHistoryResponse> response = null;
        List<OrderDtoResponse> orderDtos = new ArrayList<OrderDtoResponse>();
         this.orderStub.getUserOrderHistory(request)
                 .forEachRemaining(stubResponse -> orderDtos.add(orderMapper.orderDtoToOrderResponse( stubResponse.getOrderDto())));

         return orderDtos;

    }

//    selain stub
    private HttpHeaders createHttpHeaders(String accToken)
    {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + accToken);
        return headers;
    }

    public ResponseEntity<Map> getAccessToken(RestTemplate restTemplate){
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "password");
        map.add("username", "lintang@gmail.com");
        map.add("password", "lintang");
        map.add("client_id", "tenflix-client");
        map.add("client_secret", "y57aHOaRWrUO5PHdzk5jcUIm3RGWsKEg");
        String url = "http://keycloak-tenflix:8080/realms/tenflix/protocol/openid-connect/token";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> requestBodyFormUrlEncoded =
                new HttpEntity<>(map, headers);
        ResponseEntity<Map> responseEntity = null;
        try{
            responseEntity = restTemplate.postForEntity(url, requestBodyFormUrlEncoded, Map.class);

        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
        return responseEntity;
    }
}
