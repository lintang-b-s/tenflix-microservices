package com.lintang.netflik.movieservice.broker.message;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UpdateCreatorMessage {
    private int id;
    private String name;
    private String oldName;
}
