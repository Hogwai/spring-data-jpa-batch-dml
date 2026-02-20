package com.hogwai.springdatajpabatchdml.benchmark;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.hogwai.springdatajpabatchdml")
@EntityScan(basePackages = "com.hogwai.springdatajpabatchdml.model")
@EnableJpaRepositories(basePackages = "com.hogwai.springdatajpabatchdml.repository")
public class BenchmarkApplication {
}
