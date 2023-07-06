package com.lintang.netflik.notificationservice.service;

import com.lintang.netflik.notificationservice.dto.Message;
import com.lintang.netflik.notificationservice.dto.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public NotificationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendGlobalNotification(Message message) {
        ResponseMessage  resMessage = new ResponseMessage();
        resMessage.setMessageContentBody(message.getMessageContentBody())
                        .setMovieTitle(message.getMovieTitle())
                        .setImageUrl(message.getImageUrl());


        messagingTemplate.convertAndSend("/topic/global-notifications", resMessage);
    }


}
