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

    public PaymentEntity convertMidtransToPayment(Map<String, Any> midtransMap) {
        String orderId = (String) SerializationUtils.deserialize(midtransMap.get("order_id")
                .getValue().toByteArray());
        String transactionStatus = (String) SerializationUtils.deserialize(midtransMap.get("transaction_status")
                .getValue().toByteArray());
        String fraudStatus = (String) SerializationUtils.deserialize(midtransMap.get("fraud_status")
                .getValue().toByteArray());
        String transactionId = ((String)  SerializationUtils.deserialize(midtransMap.get("transaction_id").getValue().toByteArray()));
        ArrayList<LinkedHashMap<String,String>> vaMid = (ArrayList<LinkedHashMap<String,String>>) org.springframework.util.SerializationUtils.deserialize(midtransMap.get("va_numbers").getValue().toByteArray()); /// taruh ke watch (udah fix)

        String id = ((String)  org.springframework.util.SerializationUtils.deserialize(midtransMap.get("transaction_id").getValue().toByteArray()));
        String status= ((String)  org.springframework.util.SerializationUtils.deserialize(midtransMap.get("transaction_status").getValue().toByteArray()));
        String grossAmount = ((String) org.springframework.util.SerializationUtils.deserialize(midtransMap.get("gross_amount").getValue().toByteArray()));
        String transactionTime = ((String)  org.springframework.util.SerializationUtils.deserialize(midtransMap.get("transaction_time").getValue().toByteArray()));
        String paymentType = ((String)  org.springframework.util.SerializationUtils.deserialize(midtransMap.get("payment_type").getValue().toByteArray()));
//        String settlementType = ((String)  org.springframework.util.SerializationUtils.deserialize(midtransMap.get("settlement_time").getValue().toByteArray())); // error disini
        String bank = (vaMid.get(0).get("bank")); // error disini
        String vaNumber = (vaMid.get(0).get("va_number")); // error disini
        String currency = ((String)  org.springframework.util.SerializationUtils.deserialize(midtransMap.get("currency").getValue().toByteArray()));
        PaymentEntity paymentEntity = PaymentEntity.builder().orderId(orderId).transactionTime(transactionTime).fraudStatus(fraudStatus)
                .transactionId(transactionId).transactionStatus(transactionStatus).vaNumber(vaNumber).id(id).status(status).grossAmount(grossAmount)
                .paymentType(paymentType).bank(bank).currency(currency).paymentType(paymentType)
                .build();
        return paymentEntity;
    }




}
