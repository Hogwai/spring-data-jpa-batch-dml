package com.hogwai.springdatajpabatchdml.repository;

import com.hogwai.springdatajpabatchdml.model.Customer;

import java.util.List;

public interface CustomerCustomRepository  {
    void saveAllByBatch(List<Customer> customers);

    void updateAllByBatch(List<Customer> customers);

    List<Customer> getAllCustomers();

    void deleteAllCustomers();
}
