package com.lintang.netflik.movieservice.broker.message;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class DeleteVideoMessage {
    private int id;
}
