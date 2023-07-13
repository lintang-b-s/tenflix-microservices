package com.lintang.netflik.paymentservice.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import lombok.*;

import java.util.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Table(name = "payments")
@Entity
public class PaymentEntity {
    @Id
    private String id;
    private String status;
    private String grossAmount;
    private String transactionTime;
    private String transactionId;
    private String transactionStatus;
    private String paymentType;
    private String fraudStatus;
    private String bank;
    private String vaNumber;
    private String currency;
    private String orderId;



}
