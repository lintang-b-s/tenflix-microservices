package com.lintang.netflik.movieQueryService.broker.message;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UpdateCategoryMessage {
    private int id;
    private String name;
    private String oldName;

}
