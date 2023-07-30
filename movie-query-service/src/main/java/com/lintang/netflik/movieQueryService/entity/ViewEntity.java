package com.lintang.netflik.movieQueryService.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.LocalDateTime;

@Document("views")
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ViewEntity {

    @Id
    private int id;

    private String userId;

    @DocumentReference(lazy = false)
    private VideoEntity video;

    private int offset;

    private Platform platform;

    private LocalDateTime createdTimeStamp;

    public int getId() {
        return id;
    }

    public ViewEntity setId(int id) {
        this.id = id;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public ViewEntity setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public VideoEntity getVideo() {
        return video;
    }

    public ViewEntity setVideo(VideoEntity video) {
        this.video = video;
        return this;
    }

    public int getOffset() {
        return offset;
    }

    public ViewEntity setOffset(int offset) {
        this.offset = offset;
        return this;
    }

    public Platform getPlatform() {
        return platform;
    }

    public ViewEntity setPlatform(Platform platform) {
        this.platform = platform;
        return this;
    }

    public LocalDateTime getCreatedTimeStamp() {
        return createdTimeStamp;
    }

    public ViewEntity setCreatedTimeStamp(LocalDateTime createdTimeStamp) {
        this.createdTimeStamp = createdTimeStamp;
        return this;
    }
}
