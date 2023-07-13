package com.lintang.netflik.orderservice.command.action;

import com.google.protobuf.Any;
import com.lintang.netflik.orderservice.command.request.PaymentEntity;
import com.lintang.netflik.orderservice.command.request.VirtualAccount;
import com.lintang.netflik.orderservice.util.PaymentMapper;
import org.apache.commons.lang3.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ProcessOrderAction {
  @Autowired
  private PaymentMapper paymentMapper;

  public PaymentEntity convertMaptoHashMap(Map<String, Any> midtransMap) {
    PaymentEntity payment = paymentMapper.convertMidtransToPayment(midtransMap);

    return payment;
  }

}
