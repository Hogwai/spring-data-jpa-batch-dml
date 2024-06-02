package com.hogwai.springdatajpabatchdml.record;

import com.hogwai.springdatajpabatchdml.model.Order;
import lombok.Builder;

import java.util.Date;

@Builder
public record OrderRecord(String content,
                          Date orderDate) {
    public static OrderRecord toRecord(Order order) {
        return OrderRecord.builder()
                .content(order.getContent())
                .orderDate(order.getOrderDate())
                .build();
    }
}
