package com.lintang.netflik.orderservice.command.request;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class VirtualAccount {
    private String vaNumber;
    private String bank;
}
