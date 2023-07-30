package com.lintang.netflik.movieservice.entity;

public interface OutboxEventType {

    String ADD_MOVIE = "ADD_MOVIE";
    String UPDATE_MOVIE = "UPDATE_MOVIE";
    String DELETE_MOVIE = "DELETE_MOVIE";
    String ADD_VIDEO_TO_MOVIE = "ADD_VIDEO_TO_MOVIE";
    String UPDATE_VIDEO_FROM_MOVIE = "UPDATE_VIDEO_FROM_MOVIE";
    String DELETE_VIDEO_FROM_MOVIE = "DELETE_VIDEO_FROM_MOVIE";
    String UPDATE_TAG  = "UPDATE_TAG";
    String UPDATE_CATEGORY = "UPDATE_CATEGORY";
    String UPDATE_ACTOR = "UPDATE_ACTOR";
    String UPDATE_CREATOR= "UPDATE_CREATOR";


}
