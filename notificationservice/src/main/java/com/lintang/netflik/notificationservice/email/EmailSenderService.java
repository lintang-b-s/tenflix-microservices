package com.lintang.netflik.notificationservice.email;


public interface EmailSenderService {
    void send(String to, String movie,String email);
}

