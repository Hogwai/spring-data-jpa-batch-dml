package com.hogwai.springdatajpabatchdml.repository;

import com.hogwai.springdatajpabatchdml.model.Customer;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository
        extends HibernateRepository<Customer>, JpaRepository<Customer, Long> {

    @EntityGraph(attributePaths = {"store", "orders"})
    @Query("SELECT c FROM Customer c")
    List<Customer> findCustomersWithStoreOrders();
}
