package com.hogwai.springdatajpabatchdml.repository;

import com.hogwai.springdatajpabatchdml.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for {@link Store}.
 * Extends {@link HibernateRepository} to use explicit persist/merge operations.
 */
@Repository
public interface StoreRepository extends HibernateRepository<Store>, JpaRepository<Store, Long> {
}
