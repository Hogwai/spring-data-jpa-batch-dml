package com.hogwai.springdatajpabatchdml.service;

import com.hogwai.springdatajpabatchdml.model.Customer;

import java.util.List;

public interface CustomerService {
    void saveAllByBatch(Integer number);

    void updateAllByBatch();

    List<Customer> getAllCustomers();

    List<Customer> getAllCustomersWithStoreOrders();

    void saveAllByBatchHibernate(Integer number);

    void updateAllByBatchHibernate();

    void deleteAll();
}
