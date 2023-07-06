package com.lintang.netflik.orderaggregatorservice.entity;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {


    private String id;
    private String username;
    private String encryptedPassword;
    private String phone;
    private String email;
}
