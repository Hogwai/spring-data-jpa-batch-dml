package com.hogwai.springdatajpabatchdml.repository;

import com.hogwai.springdatajpabatchdml.model.Customer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class CustomerCustomRepository {

    private final JdbcTemplate jdbcTemplate;

    public CustomerCustomRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void saveAllByBatch(List<Customer> customers) {
        String sql = "INSERT INTO customer (id, first_name, last_name, address, city, country) " +
                "VALUES (nextval('cust_seq'), ?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(
                sql,
                customers,
                customers.size(),
                (PreparedStatement ps, Customer customer) -> {
            ps.setString(1, customer.getFirstName());
            ps.setString(2, customer.getLastName());
            ps.setString(3, customer.getAddress());
            ps.setString(4, customer.getCity());
            ps.setString(5, customer.getCountry());
        });
    }
}
