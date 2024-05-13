package com.hogwai.springdatajpabatchdml.repository;

import com.hogwai.springdatajpabatchdml.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository
        extends HibernateRepository<Customer>, JpaRepository<Customer, Long> {
}
