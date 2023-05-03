package com.lintang.netflik.notificationservice.controller;


import com.lintang.netflik.notificationservice.dto.Message;
import com.lintang.netflik.notificationservice.dto.ResponseMessage;
import com.lintang.netflik.notificationservice.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;


@Controller
public class MessageController {
    @Autowired
    private NotificationService service;

    @MessageMapping("/message")
    @SendTo("/topic/messages")
    public ResponseMessage getMessage(final Message message) throws InterruptedException {
        Thread.sleep(1000);
        service.sendGlobalNotification(message);
        return new ResponseMessage(HtmlUtils.htmlEscape(message.getMessageContentBody())
                ,HtmlUtils.htmlEscape(message.getMovieTitle()),
                HtmlUtils.htmlEscape(message.getImageUrl()));
    }

}
