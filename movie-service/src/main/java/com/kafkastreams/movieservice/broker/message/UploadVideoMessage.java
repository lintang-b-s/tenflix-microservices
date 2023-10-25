package com.kafkastreams.movieservice.broker.message;


import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode
@Builder
public class UploadVideoMessage {
    private int id;

    private byte[] file;
    private String publicId;
}
