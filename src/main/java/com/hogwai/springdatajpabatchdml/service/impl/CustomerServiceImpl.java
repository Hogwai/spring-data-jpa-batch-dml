package com.hogwai.springdatajpabatchdml.service.impl;

import com.hogwai.springdatajpabatchdml.model.Customer;
import com.hogwai.springdatajpabatchdml.model.Store;
import com.hogwai.springdatajpabatchdml.repository.CustomerCustomRepository;
import com.hogwai.springdatajpabatchdml.repository.CustomerJdbcRepository;
import com.hogwai.springdatajpabatchdml.repository.CustomerRepository;
import com.hogwai.springdatajpabatchdml.repository.StoreRepository;
import com.hogwai.springdatajpabatchdml.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import java.util.List;
import java.util.Optional;

import static com.hogwai.springdatajpabatchdml.util.CustomerFactory.generateCustomers;

@Service
public class CustomerServiceImpl implements CustomerService {

    private static final Logger log = LoggerFactory.getLogger(CustomerServiceImpl.class);
    public static final String CUSTOMER_GENERATED = "Generated customers: {}";

    private final CustomerCustomRepository customerCustomRepository;
    private final CustomerRepository customerRepository;
    private final StoreRepository storeRepository;
    private final CustomerJdbcRepository customerJdbcRepository;

    public CustomerServiceImpl(CustomerCustomRepository customerCustomRepository,
                               CustomerRepository customerRepository,
                               StoreRepository storeRepository,
                               CustomerJdbcRepository customerJdbcRepository) {
        this.customerCustomRepository = customerCustomRepository;
        this.customerRepository = customerRepository;
        this.storeRepository = storeRepository;
        this.customerJdbcRepository = customerJdbcRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Customer> getAllCustomersWithStoreOrders() {
        return customerRepository.findCustomersWithStoreOrders();
    }

    @Transactional
    @Override
    public void insertCustomersWithUnnest(Integer number) {
        Optional<Long> customerId = customerRepository.findMaxCustomerId();
        Integer maxId = customerId.map(Math::toIntExact).orElse(1);
        List<Customer> customers = generateCustomers(number, maxId);
        log.info(CUSTOMER_GENERATED, customers.size());
        Store store = storeRepository.getReferenceById(1L);
        customers.forEach(customer -> customer.setStore(store));
        customerJdbcRepository.batchInsertWithArrays(customers);
    }

    @Transactional
    @Override
    public void insertCustomersWithCopy(Integer number) {
        Optional<Long> customerId = customerRepository.findMaxCustomerId();
        Integer maxId = customerId.map(Math::toIntExact).orElse(1);
        List<Customer> customers = generateCustomers(number, maxId);
        log.info(CUSTOMER_GENERATED, customers.size());
        Store store = storeRepository.getReferenceById(1L);
        customers.forEach(customer -> customer.setStore(store));
        customerJdbcRepository.insertWithCopy(customers);
    }

    //region JDBC template
    @Override
    @Transactional
    public void saveAllByBatch(Integer number) {
        Optional<Long> customerId = customerRepository.findMaxCustomerId();
        Integer maxId = customerId.map(Math::toIntExact).orElse(1);
        List<Customer> customers = generateCustomers(number, maxId);
        log.info(CUSTOMER_GENERATED, customers.size());
        Store store = storeRepository.getReferenceById(1L);
        customers.forEach(customer -> customer.setStore(store));
        customerCustomRepository.saveAllByBatch(customers);
    }

    @Override
    @Transactional
    public void updateAllByBatch() {
        List<Customer> customersToUpdate = customerCustomRepository.getAllCustomers();
        System.out.println("Retrieved customers : " + customersToUpdate.size());

        StopWatch updateWatch = new StopWatch();
        updateWatch.start();
        customerCustomRepository.updateAllByBatch(customersToUpdate);
        log.info("Updated customers : {}", customersToUpdate.size());
        updateWatch.stop();

        System.out.println("Time elapsed for update: " + updateWatch.getTotalTimeSeconds());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Customer> getAllCustomers() {
        return customerCustomRepository.getAllCustomers();
    }
    //endregion

    //region Custom Hibernate repository
    @Override
    @Transactional
    public void saveAllByBatchHibernate(Integer number) {
        Optional<Long> customerId = customerRepository.findMaxCustomerId();
        Integer maxId = customerId.map(Math::toIntExact).orElse(1);
        List<Customer> customers = generateCustomers(number, maxId);
        log.info(CUSTOMER_GENERATED, customers.size());
        System.out.printf("Saved customers: %d %n", customerRepository.mergeAll(customers).size());
    }

    @Override
    @Transactional
    public void updateAllByBatchHibernate() {
        List<Customer> customersToUpdate = customerRepository.findAll();
        System.out.printf("Retrieved customers : %d %n", customersToUpdate.size());

        StopWatch updateWatch = new StopWatch();
        updateWatch.start();
        System.out.printf("Updated customers: %d %n ", customerRepository.updateAll(customersToUpdate).size());
        updateWatch.stop();

        System.out.printf("Time elapsed for update: %.0f %n ", updateWatch.getTotalTimeSeconds());
    }

    @Override
    @Transactional
    public void deleteAll() {
        customerCustomRepository.deleteAllOrders();
        customerCustomRepository.deleteAllCustomers();
    }
    //endregion
}
