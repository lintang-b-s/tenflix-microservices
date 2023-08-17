package com.lintang.netflik.paymentservice.query.action;


import com.lintang.netflik.models.CustomerDetails;
import com.lintang.netflik.models.ItemDetails;
import com.lintang.netflik.models.PaymentGetRedirectUrlGrpcRequest;
import com.lintang.netflik.models.TransactionDetails;
import com.lintang.netflik.paymentservice.entity.PaymentEntity;
import com.lintang.netflik.paymentservice.repository.PaymentJpaRepository;
import com.midtrans.Midtrans;
import com.midtrans.httpclient.SnapApi;
import com.midtrans.httpclient.error.MidtransError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class PaymentGrpcAction {

    @Autowired
    private PaymentJpaRepository paymentJpaRepository;

    @Value("${midtrans.serverkey}")
    private String midtransServerKey;

  /*
    find payment detail by orderId
    */
    public Optional<PaymentEntity> findByOrderId(String orderId ) {
        Optional<PaymentEntity> paymentEntity = paymentJpaRepository.findByOrderId(orderId);
        return paymentEntity;
    }
    public String getToken(PaymentGetRedirectUrlGrpcRequest request) throws MidtransError {
        Midtrans.serverKey = midtransServerKey;
        Midtrans.isProduction = false;
        String transactionToken = SnapApi.createTransactionToken(requestBodyMidtrans(request));
        return transactionToken;
    }

    public String getRedirectUrl(PaymentGetRedirectUrlGrpcRequest request) throws MidtransError {
        Midtrans.serverKey = midtransServerKey;
        Midtrans.isProduction = false;
        String redirectUrl = SnapApi.createTransactionRedirectUrl(requestBodyMidtrans(request));
        return redirectUrl;
    }

    public Map<String, Object> requestBodyMidtrans(PaymentGetRedirectUrlGrpcRequest request) {
        TransactionDetails transactionDetailsGrpc = request.getTransactionDetail();
        ItemDetails itemDetailGrpc = transactionDetailsGrpc.getItemDetails();
        CustomerDetails customerDetailsGrpc = transactionDetailsGrpc.getCustomerDetails();
        Map<String, Object> params = new HashMap<>();

        Map<String, String> transactionDetails = new HashMap<>();
        transactionDetails.put("order_id", transactionDetailsGrpc.getOrderId());
        transactionDetails.put("gross_amount", String.valueOf(transactionDetailsGrpc.getGrossAmount()));

        Map<String, String> creditCard = new HashMap<>();
        creditCard.put("secure", String.valueOf(transactionDetailsGrpc.getCreditCard()));


        List<Map<String, String>> itemDetails = new ArrayList<>();
        Map<String,String> itemDetail = new HashMap<>();
        itemDetail.put("id", String.valueOf(itemDetailGrpc.getPlanId()));
        itemDetail.put("price", String.valueOf(itemDetailGrpc.getPrice()));
        itemDetail.put("name", itemDetailGrpc.getName());
        itemDetail.put("quantity", String.valueOf(1));
        itemDetails.add(itemDetail);

        Map<String, String > customerDetail = new HashMap<>();
        customerDetail.put("first_name",  customerDetailsGrpc.getName().substring(0, customerDetailsGrpc.getName().indexOf(" ")));
        customerDetail.put("last_name",  customerDetailsGrpc.getName().substring(1, customerDetailsGrpc.getName().indexOf(" ")));
        customerDetail.put("email", customerDetailsGrpc.getEmail());
        customerDetail.put("phone", customerDetailsGrpc.getPhone());
        customerDetail.put("address", customerDetailsGrpc.getAddress()); // kayaknya salah



        params.put("transaction_details", transactionDetails);
        params.put("credit_card",  creditCard);
        params.put("item_details", itemDetails);
        params.put("customer_details", customerDetail);


        return params;
    }
}
