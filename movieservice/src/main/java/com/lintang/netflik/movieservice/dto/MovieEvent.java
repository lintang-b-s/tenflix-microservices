package com.lintang.netflik.movieservice.dto;


import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter

public class MovieEvent {
    private String status;
    private String messageContentBody;
    private String movieTitle;
    private String imageUrl;





    public MovieEvent(){}


    public MovieEvent(String status, String messageContentBody, String movieTitle, String imageUrl) {
        this.status = status;
        this.messageContentBody = messageContentBody;
        this.movieTitle = movieTitle;
        this.imageUrl = imageUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessageContentBody() {
        return messageContentBody;
    }

    public void setMessageContentBody(String messageContentBody) {
        this.messageContentBody = messageContentBody;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
