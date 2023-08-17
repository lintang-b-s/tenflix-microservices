package com.lintang.netflik.paymentservice.command.action;

import com.google.protobuf.Any;
import com.lintang.netflik.paymentservice.entity.PaymentEntity;
import okhttp3.*;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

import java.io.IOException;
import java.util.Map;

@Component
public class MidtransAction {

    //    cancel payment , if transactionStatus pending/capture
    public void  cancelPayment (PaymentEntity mapNotificationMidtrans) {
        OkHttpClient client = new OkHttpClient();
        String orderId = mapNotificationMidtrans.getOrderId();

        Request request = new Request.Builder()
                .url("https://api.sandbox.midtrans.com/v2/" + orderId + "/cancel")
                .post(null)
                .addHeader("accept", "application/json")
                .build();

        try {
            Response response = client.newCall(request).execute();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void refundPayment(PaymentEntity mapNotificationMidtrans) {
        OkHttpClient client = new OkHttpClient();
        String orderId = mapNotificationMidtrans.getOrderId();

        MediaType mediaType = MediaType.parse("application/json");
        String grossAmount = mapNotificationMidtrans.getGrossAmount();
        RequestBody body =
                RequestBody.create(mediaType, "{\"refund_key\":\"reference1\",\"amount\":" + grossAmount + ",\"reason\":\"for some reason\"}");
        Request request = new Request.Builder()
                .url("https://api.sandbox.midtrans.com/v2/" + orderId + "/refund")
                .post(body)
                .addHeader("accept", "application/json")
                .addHeader("content-type", "application/json")
                .build();

        try {
            Response response = client.newCall(request).execute();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void refundPayment(String orderId, String grossAmount ) {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body =
                RequestBody.create(mediaType, "{\"refund_key\":\"reference1\",\"amount\":" + grossAmount + ",\"reason\":\"for some reason\"}");
        Request request = new Request.Builder()
                .url("https://api.sandbox.midtrans.com/v2/" + orderId + "/refund")
                .post(body)
                .addHeader("accept", "application/json")
                .addHeader("content-type", "application/json")
                .build();

        try {
            Response response = client.newCall(request).execute();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
