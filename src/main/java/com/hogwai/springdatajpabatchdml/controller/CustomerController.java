package com.hogwai.springdatajpabatchdml.controller;

import com.hogwai.springdatajpabatchdml.model.Customer;
import com.hogwai.springdatajpabatchdml.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/save-all")
    public ResponseEntity<String> insertCustomersBatch(@RequestParam(required = false) Boolean hibernate) {
        StopWatch watch = new StopWatch();
        watch.start();
        if(Boolean.TRUE.equals(hibernate)) {
            customerService.saveAllByBatchHibernate();
        } else {
            customerService.saveAllByBatch();
        }
        watch.stop();
        System.out.println("Time elapsed for insert: " + watch.getTotalTimeSeconds());
        return ResponseEntity.ok("Inserted in " + watch.getTotalTimeSeconds());
    }

    @PutMapping("/update-all")
    public ResponseEntity<String> updateCustomersBatch(@RequestParam boolean hibernate) {
        StopWatch watch = new StopWatch();
        watch.start();
        if(hibernate) {
            customerService.updateAllByBatchHibernate();
        } else {
            customerService.updateAllByBatch();
        }

        watch.stop();
        System.out.println("Total time elapsed: " + watch.getTotalTimeSeconds());
        return ResponseEntity.ok("Updated in " + watch.getTotalTimeSeconds());
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<Customer>> getAllCustomers() {
        StopWatch watch = new StopWatch();
        watch.start();
        List<Customer> customers = customerService.getAllCustomers();
        watch.stop();
        System.out.println("Total time elapsed for getting all customers: " + watch.getTotalTimeSeconds());
        return ResponseEntity.ok(customers);
    }
}
