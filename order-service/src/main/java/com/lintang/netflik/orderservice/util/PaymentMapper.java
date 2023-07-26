package com.lintang.netflik.orderservice.util;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.Any;
import com.lintang.netflik.orderservice.command.request.PaymentEntity;
import com.lintang.netflik.orderservice.command.request.VirtualAccount;
import org.apache.commons.lang3.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class PaymentMapper {

    @Autowired
    private ObjectMapper objectMapper;
    public PaymentEntity payment;

    public PaymentEntity convertMidtransToPayment(Map<String, String> midtransMap, String  bank ,String vaNum) {
        String orderId = midtransMap.get("order_id").toString();
        String transactionStatus = midtransMap.get("transaction_status").toString();
        String fraudStatus = midtransMap.get("fraud_status").toString();
        String transactionId = midtransMap.get("transaction_id").toString();
//        ArrayList<LinkedHashMap<String,String>> vaMid = (ArrayList<LinkedHashMap<String,String>>) org.springframework.util.SerializationUtils.deserialize(midtransMap.get("va_numbers").getValue().toByteArray()); /// taruh ke watch (udah fix)


        String id = midtransMap.get("transaction_id").toString();
        String status= midtransMap.get("transaction_status").toString();
        String grossAmount = midtransMap.get("gross_amount").toString();
        String transactionTime = midtransMap.get("transaction_time").toString();
        String paymentType = midtransMap.get("payment_type").toString();
//        String settlementType = midtransMap.get("settlement_time").toString(); // error disini
        String bankE = bank;
        String vaNumber = vaNum;
        String currency = midtransMap.get("currency").toString();
        PaymentEntity paymentEntity = PaymentEntity.builder().orderId(orderId).transactionTime(transactionTime).fraudStatus(fraudStatus)
                .transactionId(transactionId).transactionStatus(transactionStatus).vaNumber(vaNumber).id(id).status(status).grossAmount(grossAmount)
                .paymentType(paymentType).bank(bankE).currency(currency).paymentType(paymentType)
                .build();
        return paymentEntity;
    }




}
