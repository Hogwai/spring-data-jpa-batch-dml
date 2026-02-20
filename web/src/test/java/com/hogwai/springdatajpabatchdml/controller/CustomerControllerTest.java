package com.hogwai.springdatajpabatchdml.controller;

import com.hogwai.springdatajpabatchdml.service.CustomerService;
import com.hogwai.springdatajpabatchdml.service.StoreService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc()
class CustomerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StoreService storeService;

    @Autowired
    @Qualifier("jdbcBatch")
    private CustomerService jdbcBatchService;

    @BeforeEach
    void create() {
        storeService.saveOne();
    }

    @AfterEach
    void deleteAll() {
        jdbcBatchService.deleteAll();
    }

    @Test
    void whenInsertingCustomers_thenCustomersAreCreated() throws Exception {
        mockMvc.perform(post("/customers/save-all")
                        .queryParam("number", String.valueOf(10000)))
                .andExpect(status().isOk());
        mockMvc.perform(get("/customers/get-all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(10000)))
                .andExpect(jsonPath("$[0].creationDate",
                        is(new SimpleDateFormat("yyyy-MM-dd").format(new Date()))))
                .andExpect(jsonPath("$[0].updateDate", is(nullValue())));
    }

    @Test
    void whenUpdatingCustomers_thenCustomersAreUpdated() throws Exception {
        mockMvc.perform(post("/customers/save-all")
                        .queryParam("number", String.valueOf(10000)))
                .andExpect(status().isOk());
        mockMvc.perform(put("/customers/update-all"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/customers/get-all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(10000)))
                .andExpect(jsonPath("$[0].creationDate",
                        is(new SimpleDateFormat("yyyy-MM-dd").format(new Date()))))
                .andExpect(jsonPath("$[0].updateDate",
                        is(new SimpleDateFormat("yyyy-MM-dd").format(new Date()))));
    }

    @Test
    void whenGettingAllCustomers_thenCustomersAreRetrieved() throws Exception {
        mockMvc.perform(post("/customers/save-all")
                        .queryParam("number", String.valueOf(10000)))
                .andExpect(status().isOk());
        mockMvc.perform(get("/customers/get-all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(10000)))
                .andExpect(jsonPath("$[0].creationDate",
                        is(new SimpleDateFormat("yyyy-MM-dd").format(new Date()))))
                .andExpect(jsonPath("$[0].updateDate", is(nullValue())));
    }

    @Test
    void whenGettingAllCustomersWithOrders_thenCustomersWithOrdersAreRetrieved() throws Exception {
        mockMvc.perform(post("/stores/save-all")
                .queryParam("number", String.valueOf(1)));
        mockMvc.perform(post("/customers/save-all")
                        .queryParam("number", String.valueOf(10000)))
                .andExpect(status().isOk());
        mockMvc.perform(get("/customers/get-all-with-store-orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(10000)))
                .andExpect(jsonPath("$[0].creationDate", is(notNullValue())))
                .andExpect(jsonPath("$[0].updateDate", is(nullValue())))
                .andExpect(jsonPath("$[0].store", is(notNullValue())));
    }

    @Test
    void whenInsertingCustomers_thenCustomersAreCreatedHibernate() throws Exception {
        mockMvc.perform(post("/customers/save-all")
                        .queryParam("mode", "hibernate")
                        .queryParam("number", String.valueOf(10000)))
                .andExpect(status().isOk());
        mockMvc.perform(get("/customers/get-all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(10000)))
                .andExpect(jsonPath("$[0].creationDate",
                        is(new SimpleDateFormat("yyyy-MM-dd").format(new Date()))))
                .andExpect(jsonPath("$[0].updateDate", is(nullValue())));
    }
}
