package com.lintang.netflik.movieQueryService.broker.message;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class DeleteMovieMessage {

    private int id;
}
