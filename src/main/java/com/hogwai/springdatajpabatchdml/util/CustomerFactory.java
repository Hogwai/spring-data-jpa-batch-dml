package com.hogwai.springdatajpabatchdml.util;

import com.hogwai.springdatajpabatchdml.model.Customer;

import java.util.ArrayList;
import java.util.List;

import static com.hogwai.springdatajpabatchdml.util.OrderFactory.generateOrder;
import static com.hogwai.springdatajpabatchdml.util.StringUtil.generateRandomString;

public class CustomerFactory {

    private CustomerFactory() {
    }

    public static List<Customer> generateCustomers(Integer number, Integer startId) {
        java.sql.Date creationDate = new java.sql.Date(new java.util.Date().getTime());
        List<Customer> customers = new ArrayList<>(number);
        for (int i = startId; i < startId + number; i++) {
            Customer customer = Customer.builder()
                    .id((long) i + 1)
                    .firstName(generateRandomString())
                    .lastName(generateRandomString())
                    .address(generateRandomString())
                    .city(generateRandomString())
                    .country(generateRandomString())
                    .creationDate(creationDate)
                    .build();
            customer.setOrders(generateOrder(1, customer));
            customers.add(customer);
        }
        return customers;
    }
}
