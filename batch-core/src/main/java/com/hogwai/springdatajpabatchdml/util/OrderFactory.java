package com.hogwai.springdatajpabatchdml.util;

import com.hogwai.springdatajpabatchdml.model.Customer;
import com.hogwai.springdatajpabatchdml.model.Order;

import java.util.ArrayList;
import java.util.List;

import static com.hogwai.springdatajpabatchdml.util.StringUtil.generateRandomString;

/**
 * Factory for generating random {@link Order} entities linked to a given {@link Customer}.
 */
public class OrderFactory {

    private OrderFactory() {
    }

    public static List<Order> generateOrder(Integer number, Customer customer) {
        java.sql.Date creationDate = new java.sql.Date(new java.util.Date().getTime());
        List<Order> orders = new ArrayList<>(number);
        for (int i = 0; i < number; i++) {
            Order order = Order.builder()
                    .content(generateRandomString())
                    .orderDate(creationDate)
                    .customer(customer)
                    .build();
            orders.add(order);
        }
        return orders;
    }
}
