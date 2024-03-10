package com.hogwai.springdatajpabatchdml.controller;

import com.hogwai.springdatajpabatchdml.model.Customer;
import com.hogwai.springdatajpabatchdml.repository.CustomerCustomRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RestController
public class CustomerController {
    private final Random random = new Random();


    private final CustomerCustomRepository customerCustomRepository;

    public CustomerController(CustomerCustomRepository customerCustomRepository) {
        this.customerCustomRepository = customerCustomRepository;
    }

    @PostMapping("/customers")
    @Transactional
    public ResponseEntity<String> insertCustomersBatch() {
        List<Customer> customers = new ArrayList<>(1000000);

        for (int i = 0; i < 100000; i++) {
            Customer customer = Customer.builder()
                    .id((long) i + 1)
                    .firstName(generateRandomString())
                    .lastName(generateRandomString())
                    .address(generateRandomString())
                    .city(generateRandomString())
                    .country(generateRandomString())
                    .build();
            customers.add(customer);
        }
        System.out.println("Generated customers: " + customers.size());
        StopWatch watch = new StopWatch();
        watch.start();
        customerCustomRepository.saveAllByBatch(customers);
        watch.stop();
        System.out.println("Time Elapsed: " + watch.getTotalTimeSeconds());
        return ResponseEntity.ok("OK");
    }

    public String generateRandomString() {
        // create a string of all characters
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        // create random string builder
        StringBuilder sb = new StringBuilder();

        // specify length of random string
        int length = 7;

        for (int i = 0; i < length; i++) {

            // generate random index number
            int index = random.nextInt(alphabet.length());

            // get character specified by index
            // from the string
            char randomChar = alphabet.charAt(index);

            // append the character to string builder
            sb.append(randomChar);
        }
        return sb.toString();
    }
}
