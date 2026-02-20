package com.hogwai.springdatajpabatchdml.integration;

import com.hogwai.springdatajpabatchdml.model.Customer;
import com.hogwai.springdatajpabatchdml.service.CustomerService;
import com.hogwai.springdatajpabatchdml.service.StoreService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("integration")
@Tag("integration")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CustomerBatchIntegrationTest {

    @Autowired
    private StoreService storeService;

    @Autowired
    @Qualifier("jdbcBatch")
    private CustomerService jdbcBatchService;

    @Autowired
    @Qualifier("hibernateBatch")
    private CustomerService hibernateBatchService;

    @Autowired
    @Qualifier("unnestBatch")
    private CustomerService unnestBatchService;

    @Autowired
    @Qualifier("copyBatch")
    private CustomerService copyBatchService;

    @BeforeAll
    void setUp() {
        storeService.saveOne();
    }

    @AfterEach
    void cleanCustomers() {
        jdbcBatchService.deleteAll();
    }

    @AfterAll
    void tearDown() {
        storeService.deleteAll();
    }

    @Test
    void testJdbcBatchInsertAndUpdate() {
        jdbcBatchService.insertBatch(100);

        List<Customer> customers = jdbcBatchService.getAll();
        assertEquals(100, customers.size());
        for (Customer c : customers) {
            assertNotNull(c.getId());
            assertNotNull(c.getFirstName());
            assertNotNull(c.getCity());
            assertNotNull(c.getCreationDate());
            assertNull(c.getUpdateDate());
        }

        jdbcBatchService.updateBatch();

        List<Customer> updated = jdbcBatchService.getAll();
        for (Customer c : updated) {
            assertNotNull(c.getUpdateDate());
        }
    }

    @Test
    void testHibernateBatchInsertAndUpdate() {
        hibernateBatchService.insertBatch(100);

        List<Customer> customers = hibernateBatchService.getAll();
        assertEquals(100, customers.size());
        for (Customer c : customers) {
            assertNotNull(c.getId());
            assertNotNull(c.getFirstName());
            assertNotNull(c.getCity());
            assertNotNull(c.getCreationDate());
            assertNull(c.getUpdateDate());
        }

        hibernateBatchService.updateBatch();

        List<Customer> updated = hibernateBatchService.getAll();
        for (Customer c : updated) {
            assertNotNull(c.getUpdateDate());
        }
    }

    @Test
    void testUnnestBatchInsert() {
        unnestBatchService.insertBatch(100);

        List<Customer> customers = unnestBatchService.getAll();
        assertEquals(100, customers.size());
        for (Customer c : customers) {
            assertNotNull(c.getId());
            assertNotNull(c.getFirstName());
            assertNotNull(c.getCity());
            assertNotNull(c.getCreationDate());
        }
    }

    @Test
    void testCopyBatchInsert() {
        copyBatchService.insertBatch(100);

        List<Customer> customers = copyBatchService.getAll();
        assertEquals(100, customers.size());
        for (Customer c : customers) {
            assertNotNull(c.getId());
            assertNotNull(c.getFirstName());
            assertNotNull(c.getCity());
            assertNotNull(c.getCreationDate());
        }
    }
}
