package com.hogwai.springdatajpabatchdml.controller;

import com.hogwai.springdatajpabatchdml.model.Customer;
import com.hogwai.springdatajpabatchdml.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/customers")
    public ResponseEntity<String> insertCustomersBatch() {
        StopWatch watch = new StopWatch();
        watch.start();
        customerService.saveAllByBatch();
        watch.stop();
        System.out.println("Time elapsed for insert: " + watch.getTotalTimeSeconds());
        return ResponseEntity.ok("Inserted in " + watch.getTotalTimeSeconds());
    }

    @PutMapping("/customers")
    public ResponseEntity<String> updateCustomersBatch() {
        StopWatch watch = new StopWatch();
        watch.start();
        customerService.updateAllByBatch();
        watch.stop();
        System.out.println("Total time elapsed: " + watch.getTotalTimeSeconds());
        return ResponseEntity.ok("Updated in " + watch.getTotalTimeSeconds());
    }

    @GetMapping("/customers")
    public ResponseEntity<List<Customer>> getAllCustomers() {
        StopWatch watch = new StopWatch();
        watch.start();
        List<Customer> customers = customerService.getAllCustomers();
        watch.stop();
        System.out.println("Total time elapsed for getting all customers: " + watch.getTotalTimeSeconds());
        return ResponseEntity.ok(customers);
    }
}
