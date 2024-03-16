package com.hogwai.springdatajpabatchdml.service.impl;

import com.hogwai.springdatajpabatchdml.model.Customer;
import com.hogwai.springdatajpabatchdml.repository.CustomerCustomRepository;
import com.hogwai.springdatajpabatchdml.service.CustomerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final Random random = new Random();

    private final CustomerCustomRepository customerCustomRepository;

    public CustomerServiceImpl(CustomerCustomRepository customerCustomRepository) {
        this.customerCustomRepository = customerCustomRepository;
    }

    @Override
    @Transactional
    public void saveAllByBatch(){
        List<Customer> customers = new ArrayList<>(100000);
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
        customerCustomRepository.saveAllByBatch(customers);
    }

    @Override
    @Transactional
    public void updateAllByBatch(){
        List<Customer> customersToUpdate = customerCustomRepository.getAllCustomers();
        System.out.println("Retrieved customers : " + customersToUpdate.size());

        StopWatch updateWatch = new StopWatch();
        updateWatch.start();
        customerCustomRepository.updateAllByBatch(customersToUpdate);
        System.out.println("Updated customers : " + customersToUpdate.size());
        updateWatch.stop();

        System.out.println("Time elapsed for update: " + updateWatch.getTotalTimeSeconds());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Customer> getAllCustomers() {
        return customerCustomRepository.getAllCustomers();
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
