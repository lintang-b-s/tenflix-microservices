package com.lintang.netflik.orderaggregatorservice.command.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class KeycloakUserDto {
    private String id;
    private String username;

    private String firstName;
    private String lastName;
    private String email;


}
