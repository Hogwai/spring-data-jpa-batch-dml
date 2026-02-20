package com.hogwai.springdatajpabatchdml.service.impl;

import com.hogwai.springdatajpabatchdml.model.Customer;
import com.hogwai.springdatajpabatchdml.model.Store;
import com.hogwai.springdatajpabatchdml.repository.CustomerCustomRepository;
import com.hogwai.springdatajpabatchdml.repository.CustomerRepository;
import com.hogwai.springdatajpabatchdml.repository.StoreRepository;
import com.hogwai.springdatajpabatchdml.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import java.util.List;

import static com.hogwai.springdatajpabatchdml.util.CustomerFactory.generateCustomers;

/**
 * {@link CustomerService} implementation that uses Hibernate's built-in batching.
 *
 * <p>Qualified as {@code "hibernateBatch"}. Persists entities through
 * {@link CustomerRepository#persistAllAndFlush}, which relies on the JPA
 * {@link jakarta.persistence.EntityManager} and the configured JDBC batch size
 * to group INSERT statements.</p>
 */
@Service("hibernateBatch")
public class HibernateBatchCustomerService implements CustomerService {

    private static final Logger log = LoggerFactory.getLogger(HibernateBatchCustomerService.class);

    private final CustomerRepository customerRepository;
    private final StoreRepository storeRepository;
    private final CustomerCustomRepository customerCustomRepository;

    public HibernateBatchCustomerService(CustomerRepository customerRepository,
                                         StoreRepository storeRepository,
                                         CustomerCustomRepository customerCustomRepository) {
        this.customerRepository = customerRepository;
        this.storeRepository = storeRepository;
        this.customerCustomRepository = customerCustomRepository;
    }

    @Override
    @Transactional
    public void insertBatch(Integer number) {
        List<Customer> customers = generateCustomers(number);

        log.info("Generated customers: {}", customers.size());
        Store store = storeRepository.getReferenceById(1L);
        customers.forEach(customer -> customer.setStore(store));
        log.info("Saved customers: {}", customerRepository.persistAllAndFlush(customers).size());
    }

    @Override
    @Transactional
    public void updateBatch() {
        List<Customer> customersToUpdate = customerRepository.findAll();
        log.info("Retrieved customers: {}", customersToUpdate.size());

        java.util.Date now = new java.sql.Date(System.currentTimeMillis());
        customersToUpdate.forEach(c -> c.setUpdateDate(now));

        StopWatch updateWatch = new StopWatch();
        updateWatch.start();
        log.info("Updated customers: {}", customerRepository.updateAll(customersToUpdate).size());
        updateWatch.stop();

        log.info("Time elapsed for update: {}", updateWatch.getTotalTimeSeconds());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Customer> getAll() {
        return customerRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteAll() {
        customerCustomRepository.deleteAllOrders();
        customerCustomRepository.deleteAllCustomers();
    }
}
