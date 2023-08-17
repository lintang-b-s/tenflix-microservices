package com.lintang.netflik.paymentservice.command.action;

import com.google.protobuf.Any;
import com.lintang.netflik.paymentservice.broker.message.PaymentTransactionErrorMessage;
import com.lintang.netflik.paymentservice.broker.message.PaymentValidatedMessage;
import com.lintang.netflik.paymentservice.entity.PaymentEntity;
import com.lintang.netflik.paymentservice.entity.VirtualAccount;
import com.lintang.netflik.paymentservice.repository.PaymentJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.SerializationUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Component
public class PaymentSagaAction {
    @Autowired
    private PaymentJpaRepository paymentJpaRepository;



    public void savePayment(PaymentEntity paymentEntity) {

        PaymentEntity payment = paymentEntity;
        this.paymentJpaRepository.save(payment);
    }


    public Optional<PaymentEntity> getPaymentById(String id) {
        Optional<PaymentEntity> payment = paymentJpaRepository.findById(id);
        return payment;
    }

    public void deletePaymentById(String id) {
        paymentJpaRepository.deleteById(id);
    }
}
