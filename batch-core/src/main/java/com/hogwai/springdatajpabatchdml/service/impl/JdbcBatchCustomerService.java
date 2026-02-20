package com.hogwai.springdatajpabatchdml.service.impl;

import com.hogwai.springdatajpabatchdml.model.Customer;
import com.hogwai.springdatajpabatchdml.model.Store;
import com.hogwai.springdatajpabatchdml.repository.CustomerCustomRepository;
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
 * {@link CustomerService} implementation that uses {@link JdbcTemplate} batch updates.
 *
 * <p>Qualified as {@code "jdbcBatch"}. Delegates insert and update operations
 * to {@link CustomerCustomRepository}, which issues multi-row prepared statements
 * through {@link org.springframework.jdbc.core.JdbcTemplate#batchUpdate}.</p>
 */
@Service("jdbcBatch")
public class JdbcBatchCustomerService implements CustomerService {

    private static final Logger log = LoggerFactory.getLogger(JdbcBatchCustomerService.class);

    private final CustomerCustomRepository customerCustomRepository;
    private final StoreRepository storeRepository;

    public JdbcBatchCustomerService(CustomerCustomRepository customerCustomRepository,
                                    StoreRepository storeRepository) {
        this.customerCustomRepository = customerCustomRepository;
        this.storeRepository = storeRepository;
    }

    @Override
    @Transactional
    public void insertBatch(Integer number) {
        List<Customer> customers = generateCustomers(number);
        log.info("Generated customers: {}", customers.size());
        Store store = storeRepository.getReferenceById(1L);
        customers.forEach(customer -> customer.setStore(store));
        customerCustomRepository.saveAllByBatch(customers);
    }

    @Override
    @Transactional
    public void updateBatch() {
        List<Customer> customersToUpdate = customerCustomRepository.getAllCustomers();
        log.info("Retrieved customers: {}", customersToUpdate.size());

        StopWatch updateWatch = new StopWatch();
        updateWatch.start();
        customerCustomRepository.updateAllByBatch(customersToUpdate);
        log.info("Updated customers: {}", customersToUpdate.size());
        updateWatch.stop();

        log.info("Time elapsed for update: {}", updateWatch.getTotalTimeSeconds());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Customer> getAll() {
        return customerCustomRepository.getAllCustomers();
    }

    @Override
    @Transactional
    public void deleteAll() {
        customerCustomRepository.deleteAllOrders();
        customerCustomRepository.deleteAllCustomers();
    }
}
