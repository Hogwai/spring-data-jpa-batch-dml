package com.hogwai.springdatajpabatchdml.service;

import com.hogwai.springdatajpabatchdml.model.Customer;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CustomerService {
    void insertCustomersWithUnnest(Integer number);

    @Transactional
    void insertCustomersWithCopy(Integer number);

    void saveAllByBatch(Integer number);

    void updateAllByBatch();

    List<Customer> getAllCustomers();

    List<Customer> getAllCustomersWithStoreOrders();

    void saveAllByBatchHibernate(Integer number);

    void updateAllByBatchHibernate();

    void deleteAll();
}
