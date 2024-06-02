package com.hogwai.springdatajpabatchdml.record;

import com.hogwai.springdatajpabatchdml.model.Customer;
import lombok.Builder;

import java.util.Date;
import java.util.List;

@Builder
public record CustomerRecord(String firstName,
                             String lastName,
                             String country,
                             String address,
                             String city,
                             Date creationDate,
                             Date updateDate,
                             String store,
                             List<OrderRecord> orders) {
    public static CustomerRecord toRecord(Customer customer) {
        return CustomerRecord.builder()
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .address(customer.getAddress())
                .city(customer.getCity())
                .creationDate(customer.getCreationDate())
                .updateDate(customer.getUpdateDate())
                .store(customer.getStore().getName())
                .orders(customer.getOrders().stream().map(OrderRecord::toRecord).toList())
                .build();
    }
}
