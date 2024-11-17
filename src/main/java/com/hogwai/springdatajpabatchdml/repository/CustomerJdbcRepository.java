package com.hogwai.springdatajpabatchdml.repository;

import com.hogwai.springdatajpabatchdml.model.Customer;

import java.util.List;

public interface CustomerJdbcRepository {
    void batchInsertWithArrays(List<Customer> customers);

    // Alternative avec copy
    void insertWithCopy(List<Customer> customers);
}
