package com.hogwai.springdatajpabatchdml.repository;

import com.hogwai.springdatajpabatchdml.model.Customer;
import com.hogwai.springdatajpabatchdml.projection.CustomerProjection;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Spring Data JPA repository for {@link Customer}.
 * Extends {@link HibernateRepository} to replace the default {@code save} methods
 * with explicit {@code persist}/{@code merge}/{@code update} operations.
 */
@Repository
public interface CustomerRepository
        extends HibernateRepository<Customer>, JpaRepository<Customer, Long> {

    /**
     * Fetches all customers with their store and orders eagerly loaded
     * using an {@link EntityGraph}.
     *
     * @return all customers with associated store and orders
     */
    @EntityGraph(attributePaths = {"store", "orders"})
    @Query("SELECT c FROM Customer c")
    List<Customer> findCustomersWithStoreOrders();

    /**
     * Returns all customers as {@link CustomerProjection} with store and orders
     * fetched via a JPQL JOIN FETCH query.
     *
     * @return projected view of all customers
     */
    @Query("""
        SELECT distinct c FROM Customer c
        LEFT JOIN FETCH c.store
        LEFT JOIN FETCH c.orders
    """)
    @Transactional(readOnly = true)
    List<CustomerProjection> findAllProjectedBy();

}
