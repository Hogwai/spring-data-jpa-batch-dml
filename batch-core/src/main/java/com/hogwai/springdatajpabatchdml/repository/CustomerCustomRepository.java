package com.hogwai.springdatajpabatchdml.repository;

import com.hogwai.springdatajpabatchdml.model.Customer;

import java.util.List;

/**
 * Custom repository for {@link Customer} using {@link org.springframework.jdbc.core.JdbcTemplate}
 * for batch insert and update operations with native SQL.
 */
public interface CustomerCustomRepository  {

    /**
     * Inserts all customers in a single JDBC batch using {@code batchUpdate}.
     *
     * @param customers the customers to insert
     */
    void saveAllByBatch(List<Customer> customers);

    /**
     * Updates city, country and update date for all customers in a single JDBC batch.
     *
     * @param customers the customers to update
     */
    void updateAllByBatch(List<Customer> customers);

    /**
     * Retrieves all customers using a plain JDBC query.
     *
     * @return all customers
     */
    List<Customer> getAllCustomers();

    /**
     * Deletes all rows from the customer table.
     */
    void deleteAllCustomers();

    /**
     * Deletes all rows from the orders table.
     */
    void deleteAllOrders();
}
