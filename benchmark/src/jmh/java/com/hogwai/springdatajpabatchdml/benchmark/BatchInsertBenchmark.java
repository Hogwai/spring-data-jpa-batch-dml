package com.hogwai.springdatajpabatchdml.benchmark;

import com.hogwai.springdatajpabatchdml.service.CustomerService;
import com.hogwai.springdatajpabatchdml.service.StoreService;
import org.openjdk.jmh.annotations.*;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class BatchInsertBenchmark {

    private ConfigurableApplicationContext context;

    private CustomerService jdbcBatchService;
    private CustomerService hibernateBatchService;
    private CustomerService unnestBatchService;
    private CustomerService copyBatchService;
    private CustomerService deleteService;
    private StoreService storeService;

    @Param({"1000"})
    private int numberOfCustomers;

    @Setup(Level.Trial)
    public void setUp() {
        context = SpringApplication.run(BenchmarkApplication.class);
        jdbcBatchService = context.getBean("jdbcBatch", CustomerService.class);
        hibernateBatchService = context.getBean("hibernateBatch", CustomerService.class);
        unnestBatchService = context.getBean("unnestBatch", CustomerService.class);
        copyBatchService = context.getBean("copyBatch", CustomerService.class);
        deleteService = jdbcBatchService;
        storeService = context.getBean(StoreService.class);
        storeService.saveOne();
    }

    @TearDown(Level.Iteration)
    public void cleanUp() {
        deleteService.deleteAll();
    }

    @TearDown(Level.Trial)
    public void tearDown() {
        storeService.deleteAll();
        context.close();
    }

    @Benchmark
    public void jdbcBatchInsert() {
        jdbcBatchService.insertBatch(numberOfCustomers);
    }

    @Benchmark
    public void hibernateBatchInsert() {
        hibernateBatchService.insertBatch(numberOfCustomers);
    }

    @Benchmark
    public void unnestBatchInsert() {
        unnestBatchService.insertBatch(numberOfCustomers);
    }

    @Benchmark
    public void copyBatchInsert() {
        copyBatchService.insertBatch(numberOfCustomers);
    }
}
