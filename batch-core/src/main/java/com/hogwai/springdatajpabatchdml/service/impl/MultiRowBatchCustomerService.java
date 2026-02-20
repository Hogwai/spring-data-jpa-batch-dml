package com.hogwai.springdatajpabatchdml.service.impl;

import com.hogwai.springdatajpabatchdml.model.Customer;
import com.hogwai.springdatajpabatchdml.model.Store;
import com.hogwai.springdatajpabatchdml.repository.CustomerCustomRepository;
import com.hogwai.springdatajpabatchdml.repository.CustomerJdbcRepository;
import com.hogwai.springdatajpabatchdml.repository.StoreRepository;
import com.hogwai.springdatajpabatchdml.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.hogwai.springdatajpabatchdml.util.CustomerFactory.generateCustomers;

/**
 * {@link CustomerService} implementation that uses multi-row {@code INSERT INTO ... VALUES}.
 *
 * <p>Qualified as {@code "multiRowBatch"}. Builds a single SQL statement with
 * multiple {@code (?, ?, ...)} value tuples, letting the database insert all rows
 * in one round-trip without PostgreSQL-specific extensions.</p>
 */
@Service("multiRowBatch")
public class MultiRowBatchCustomerService implements CustomerService {

    private static final Logger log = LoggerFactory.getLogger(MultiRowBatchCustomerService.class);

    private final CustomerJdbcRepository customerJdbcRepository;
    private final StoreRepository storeRepository;
    private final CustomerCustomRepository customerCustomRepository;

    public MultiRowBatchCustomerService(CustomerJdbcRepository customerJdbcRepository,
                                        StoreRepository storeRepository,
                                        CustomerCustomRepository customerCustomRepository) {
        this.customerJdbcRepository = customerJdbcRepository;
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
        customerJdbcRepository.insertWithMultiRowValues(customers);
    }

    @Override
    public void updateBatch() {
        throw new UnsupportedOperationException("Update not supported for multi-row VALUES strategy");
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
