package com.lintang.netflik.paymentservice.query.service;


import com.lintang.netflik.paymentservice.*;
import com.lintang.netflik.paymentservice.config.BadRequestException;
import com.lintang.netflik.paymentservice.query.action.PaymentGrpcAction;
import com.lintang.netflik.paymentservice.repository.PaymentJpaRepository;
import com.lintang.netflik.models.CustomerDetails;
import com.lintang.netflik.models.*;
import com.midtrans.Midtrans;
import com.midtrans.httpclient.SnapApi;
import com.midtrans.httpclient.error.MidtransError;
import io.github.cdimascio.dotenv.Dotenv;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.*;

@GrpcService
public class PaymentGrpcService extends PaymentServiceGrpc.PaymentServiceImplBase {
    @Autowired
    private PaymentJpaRepository paymentJpaRepository;

    @Value("${midtrans.serverkey}")
    private String midtransServerKey;

    @Autowired
    private PaymentGrpcAction paymentGrpcAction;

    @Override
    public void getPaymentRedirectUrl(PaymentGetRedirectUrlGrpcRequest request, StreamObserver<PaymentGetRedirectUrlGrpcResponse> responseObersver)
            {
        PaymentGetRedirectUrlGrpcResponse.Builder builder = PaymentGetRedirectUrlGrpcResponse.newBuilder();
        Midtrans.serverKey = midtransServerKey;
        Midtrans.isProduction = false;
        String transactionToken =null;
        String redirectUrl =null;
        try{
            transactionToken = paymentGrpcAction.getToken(request);
            redirectUrl = paymentGrpcAction.getRedirectUrl(request);
        }catch (MidtransError e) {
            responseObersver.onError(Status.INVALID_ARGUMENT.withDescription("Midtrans Error?").asRuntimeException());
        }


        builder.setGetRedirectUrl(GetRedirectUrl.newBuilder().setRedirectUrl(redirectUrl)
                        .setToken(transactionToken)
                .build());
        responseObersver.onNext(builder.build());
        responseObersver.onCompleted();
    }


}
