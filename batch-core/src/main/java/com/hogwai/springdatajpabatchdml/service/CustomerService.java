package com.hogwai.springdatajpabatchdml.service;

import com.hogwai.springdatajpabatchdml.model.Customer;

import java.util.List;

/**
 * Service interface for batch operations on {@link Customer} entities.
 * Four implementations exist, each using a different insert strategy:
 * JDBC batch, Hibernate batch, UNNEST and COPY.
 *
 * @see com.hogwai.springdatajpabatchdml.service.impl.JdbcBatchCustomerService
 * @see com.hogwai.springdatajpabatchdml.service.impl.HibernateBatchCustomerService
 * @see com.hogwai.springdatajpabatchdml.service.impl.UnnestBatchCustomerService
 * @see com.hogwai.springdatajpabatchdml.service.impl.CopyBatchCustomerService
 */
public interface CustomerService {

    /**
     * Generates and inserts the given number of customers in batch.
     *
     * @param number the number of customers to generate and insert
     */
    void insertBatch(Integer number);

    /**
     * Updates all existing customers in batch.
     */
    void updateBatch();

    /**
     * Retrieves all customers.
     *
     * @return all customers
     */
    List<Customer> getAll();

    /**
     * Deletes all customers and their orders.
     */
    void deleteAll();
}
