package com.hans.study.kafka.dto;

import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;

@Builder
@Getter
public class OrderReq {
    private final String orderId;

    private final String productName;

    private final Integer quantity;

    private final String userId;

    private final Integer amount;

    private final Timestamp paymentTime;
}