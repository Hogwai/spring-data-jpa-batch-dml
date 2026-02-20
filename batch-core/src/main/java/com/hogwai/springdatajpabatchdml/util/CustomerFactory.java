package com.hogwai.springdatajpabatchdml.util;

import com.hogwai.springdatajpabatchdml.model.Customer;

import java.util.ArrayList;
import java.util.List;

import static com.hogwai.springdatajpabatchdml.util.OrderFactory.generateOrder;
import static com.hogwai.springdatajpabatchdml.util.StringUtil.generateRandomString;

/**
 * Factory for generating random {@link Customer} entities with associated orders.
 *
 * <p>Each generated customer is populated with random strings for all fields
 * and one {@link com.hogwai.springdatajpabatchdml.model.Order}.</p>
 */
public class CustomerFactory {

    private CustomerFactory() {
    }

    public static List<Customer> generateCustomers(Integer number) {
        java.sql.Date creationDate = new java.sql.Date(new java.util.Date().getTime());
        List<Customer> customers = new ArrayList<>(number);
        for (int i = 0; i < number; i++) {
            Customer customer = Customer.builder()
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
