package com.hogwai.springdatajpabatchdml.service;

import com.hogwai.springdatajpabatchdml.model.Customer;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CustomerService {
    @Transactional
    void saveAllByBatch(Integer number);

    @Transactional
    void updateAllByBatch();

    @Transactional(readOnly = true)
    List<Customer> getAllCustomers();

    @Transactional
    void saveAllByBatchHibernate(Integer number);

    @Transactional
    void updateAllByBatchHibernate();

    @Transactional
    void deleteAll();
}
