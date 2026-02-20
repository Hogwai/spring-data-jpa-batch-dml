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
 * {@link CustomerService} implementation that uses PostgreSQL's {@code UNNEST} function.
 *
 * <p>Qualified as {@code "unnestBatch"}. Passes each column as a PostgreSQL array
 * and inserts all rows in a single {@code INSERT ... SELECT FROM UNNEST(...)} statement,
 * avoiding multiple round-trips to the database.</p>
 */
@Service("unnestBatch")
public class UnnestBatchCustomerService implements CustomerService {

    private static final Logger log = LoggerFactory.getLogger(UnnestBatchCustomerService.class);

    private final CustomerJdbcRepository customerJdbcRepository;
    private final StoreRepository storeRepository;
    private final CustomerCustomRepository customerCustomRepository;

    public UnnestBatchCustomerService(CustomerJdbcRepository customerJdbcRepository,
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
        customerJdbcRepository.batchInsertWithArrays(customers);
    }

    @Override
    public void updateBatch() {
        throw new UnsupportedOperationException("Update not supported for UNNEST strategy");
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
