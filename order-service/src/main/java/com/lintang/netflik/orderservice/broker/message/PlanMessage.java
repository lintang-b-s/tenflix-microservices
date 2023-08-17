package com.lintang.netflik.orderservice.broker.message;

import com.lintang.netflik.orderservice.entity.OrderEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.OneToOne;
import java.math.BigDecimal;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PlanMessage {

    private Long planId;
    private Long price;
    private Long subTotal;
    private String name;
    private String description;
    private int activePeriod;
    private int discountPrice;
}
