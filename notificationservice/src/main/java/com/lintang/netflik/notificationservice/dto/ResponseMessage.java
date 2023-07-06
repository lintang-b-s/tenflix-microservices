package com.lintang.netflik.notificationservice.dto;

public class ResponseMessage {
    private String messageContentBody;
    private String movieTitle;
    private String imageUrl;

    public ResponseMessage(){}

    public ResponseMessage(String messageContentBody, String movieTitle, String imageUrl) {
        this.messageContentBody = messageContentBody;
        this.movieTitle = movieTitle;
        this.imageUrl = imageUrl;
    }

    public String getMessageContentBody() {
        return messageContentBody;
    }

    public ResponseMessage setMessageContentBody(String messageContentBody) {
        this.messageContentBody = messageContentBody;
        return this;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public ResponseMessage setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public ResponseMessage setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }
}
