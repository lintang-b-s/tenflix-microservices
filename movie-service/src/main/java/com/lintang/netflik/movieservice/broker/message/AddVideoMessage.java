package com.lintang.netflik.movieservice.broker.message;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AddVideoMessage {
    private int id;
    private String url;
    private String publicId;
    private int length;
    private String title;
    private String synopsis;
    private String movieId;

}
