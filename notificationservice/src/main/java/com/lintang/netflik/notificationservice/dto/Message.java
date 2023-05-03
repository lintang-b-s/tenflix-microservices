package com.lintang.netflik.notificationservice.dto;

public class Message {
    private String messageContentBody;
    private String movieTitle;
    private String imageUrl;

    public Message(){}
    public Message(String messageContentBody, String movieTitle, String imageUrl) {
        this.messageContentBody = messageContentBody;
        this.movieTitle = movieTitle;
        this.imageUrl = imageUrl;
    }

    public String getMessageContentBody() {
        return messageContentBody;
    }

    public Message setMessageContentBody(String messageContentBody) {
        this.messageContentBody = messageContentBody;
        return this;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public Message setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Message setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }
}
