package com.hogwai.springdatajpabatchdml.service;

import com.hogwai.springdatajpabatchdml.model.Store;

import java.util.List;

/**
 * Service interface for {@link Store} CRUD operations.
 */
public interface StoreService {

    /**
     * Generates and persists the given number of stores.
     *
     * @param number the number of stores to create
     */
    void saveAll(Integer number);

    /**
     * Creates and persists a single store with a random name.
     */
    void saveOne();

    /**
     * Retrieves all stores.
     *
     * @return all stores
     */
    List<Store> getAll();

    /**
     * Deletes all stores.
     */
    void deleteAll();
}
