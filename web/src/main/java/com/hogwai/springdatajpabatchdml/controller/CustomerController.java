package com.hogwai.springdatajpabatchdml.controller;

import com.hogwai.springdatajpabatchdml.model.Customer;
import com.hogwai.springdatajpabatchdml.projection.CustomerProjection;
import com.hogwai.springdatajpabatchdml.record.CustomerRecord;
import com.hogwai.springdatajpabatchdml.repository.CustomerRepository;
import com.hogwai.springdatajpabatchdml.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private static final Logger log = LoggerFactory.getLogger(CustomerController.class);

    private final CustomerService jdbcBatchService;
    private final CustomerService hibernateBatchService;
    private final CustomerService unnestBatchService;
    private final CustomerService copyBatchService;
    private final CustomerService multiRowBatchService;
    private final CustomerRepository customerRepository;

    public CustomerController(@Qualifier("jdbcBatch") CustomerService jdbcBatchService,
                              @Qualifier("hibernateBatch") CustomerService hibernateBatchService,
                              @Qualifier("unnestBatch") CustomerService unnestBatchService,
                              @Qualifier("copyBatch") CustomerService copyBatchService,
                              @Qualifier("multiRowBatch") CustomerService multiRowBatchService,
                              CustomerRepository customerRepository) {
        this.jdbcBatchService = jdbcBatchService;
        this.hibernateBatchService = hibernateBatchService;
        this.unnestBatchService = unnestBatchService;
        this.copyBatchService = copyBatchService;
        this.multiRowBatchService = multiRowBatchService;
        this.customerRepository = customerRepository;
    }

    @PostMapping("/save-all")
    public ResponseEntity<String> insertCustomersBatch(@RequestParam(required = false) String mode,
                                                       @RequestParam Integer number) {
        StopWatch watch = new StopWatch();
        watch.start();
        resolveService(mode).insertBatch(number);
        watch.stop();
        log.info("Time elapsed for insert: {}", watch.getTotalTimeSeconds());
        return ResponseEntity.ok("Inserted in " + watch.getTotalTimeSeconds());
    }

    @PutMapping("/update-all")
    public ResponseEntity<String> updateCustomersBatch(@RequestParam(required = false) String mode) {
        StopWatch watch = new StopWatch();
        watch.start();
        resolveService(mode).updateBatch();
        watch.stop();
        log.info("Total time elapsed: {}", watch.getTotalTimeSeconds());
        return ResponseEntity.ok("Updated in " + watch.getTotalTimeSeconds());
    }

    @GetMapping("/get-all-with-store-orders")
    public ResponseEntity<List<CustomerRecord>> getAllCustomersWithStore() {
        StopWatch watch = new StopWatch();
        watch.start();
        List<Customer> customers = customerRepository.findCustomersWithStoreOrders();
        List<CustomerRecord> customerRecords = customers.stream()
                .map(CustomerRecord::toRecord)
                .toList();
        watch.stop();
        log.info("Total time elapsed for getting all customers: {}", watch.getTotalTimeSeconds());
        return ResponseEntity.ok(customerRecords);
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<Customer>> getAllCustomers() {
        StopWatch watch = new StopWatch();
        watch.start();
        List<Customer> customers = jdbcBatchService.getAll();
        watch.stop();
        log.info("Total time elapsed for getting all customers: {}", watch.getTotalTimeSeconds());
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/get-all-projections")
    public ResponseEntity<List<CustomerProjection>> getAllCustomerProjections() {
        StopWatch watch = new StopWatch();
        watch.start();
        List<CustomerProjection> customers = customerRepository.findAllProjectedBy();
        watch.stop();
        log.info("Total time elapsed for getting all customers: {}", watch.getTotalTimeSeconds());
        return ResponseEntity.ok(customers);
    }

    @DeleteMapping("/delete-all")
    public ResponseEntity<String> deleteAllCustomers() {
        StopWatch watch = new StopWatch();
        watch.start();
        jdbcBatchService.deleteAll();
        watch.stop();
        log.info("Total time elapsed for deleting all customers: {}", watch.getTotalTimeSeconds());
        return ResponseEntity.ok().body("Deleted all customers");
    }

    private CustomerService resolveService(String mode) {
        if (mode == null) {
            return jdbcBatchService;
        }
        return switch (mode) {
            case "hibernate" -> hibernateBatchService;
            case "unnest" -> unnestBatchService;
            case "copy" -> copyBatchService;
            case "multirow" -> multiRowBatchService;
            default -> jdbcBatchService;
        };
    }
}
