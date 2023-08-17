package com.lintang.netflik.movieservice.broker.message;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UploadedVideoMessage {
    private String url;

    private String publicId;
    private int id;
}
