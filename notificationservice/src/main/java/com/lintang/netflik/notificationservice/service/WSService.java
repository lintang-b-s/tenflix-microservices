package com.lintang.netflik.notificationservice.service;

import com.lintang.netflik.notificationservice.dto.Message;
import com.lintang.netflik.notificationservice.dto.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WSService {
    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationService notificationService;

    @Autowired
    public WSService(SimpMessagingTemplate messagingTemplate, NotificationService notificationService) {
        this.messagingTemplate = messagingTemplate;
        this.notificationService = notificationService;
    }

    public void notifyFrontend(final Message message) {
        ResponseMessage response = new ResponseMessage();
        response.setMessageContentBody(message.getMessageContentBody())
                .setMovieTitle(message.getMovieTitle())
                .setImageUrl(message.getImageUrl());

        notificationService.sendGlobalNotification(message);

        messagingTemplate.convertAndSend("/topic/messages", response);
    }



}
