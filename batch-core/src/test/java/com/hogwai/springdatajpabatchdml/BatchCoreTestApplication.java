package com.hogwai.springdatajpabatchdml;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.hogwai.springdatajpabatchdml.repository")
public class BatchCoreTestApplication {
}
