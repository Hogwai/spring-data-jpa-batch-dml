package com.hogwai.springdatajpabatchdml.repository;

import com.hogwai.springdatajpabatchdml.model.Customer;

import java.util.List;

/**
 * Repository for {@link Customer} using PostgreSQL-specific batch insert strategies.
 */
public interface CustomerJdbcRepository {

    /**
     * Inserts customers using PostgreSQL {@code UNNEST} — passes each column as a SQL array
     * and inserts all rows in a single statement.
     *
     * @param customers the customers to insert
     */
    void batchInsertWithArrays(List<Customer> customers);

    /**
     * Inserts customers using PostgreSQL {@code COPY ... FROM STDIN} protocol,
     * writing tab-separated values through {@link org.postgresql.copy.CopyManager}.
     *
     * @param customers the customers to insert
     */
    void insertWithCopy(List<Customer> customers);

    /**
     * Inserts customers using a multi-row {@code INSERT INTO ... VALUES (...), (...), ...}
     * statement. Rows are batched to avoid exceeding the maximum SQL statement size.
     *
     * @param customers the customers to insert
     */
    void insertWithMultiRowValues(List<Customer> customers);
}
