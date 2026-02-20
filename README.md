# Spring Data JPA Batch DML

Comparison of 4 batch insert strategies using Spring Data JPA and PostgreSQL.

## Strategies

| Strategy | Description |
|----------|-------------|
| **Hibernate Batch** | `EntityManager.persist()` + flush with `hibernate.jdbc.batch_size` and `reWriteBatchedInserts` |
| **JDBC Batch** | `JdbcTemplate.batchUpdate()` with native SQL |
| **UNNEST** | `INSERT ... SELECT FROM UNNEST(...)` — single SQL statement using PostgreSQL arrays |
| **COPY** | `COPY ... FROM STDIN` — PostgreSQL binary protocol via `CopyManager` |

## Project Structure

```
spring-data-jpa-batch-dml/
├── batch-core/          # Models, repositories, services, utilities (JAR)
├── web/                 # Spring Boot app, controllers, records (bootJar)
├── benchmark/           # JMH benchmarks
└── docker-compose.yml   # PostgreSQL 17
```

### Modules

- **batch-core** — shared business logic (JPA entities, repositories, 4 `CustomerService` implementations)
- **web** — Spring Boot application with REST API (`/customers`, `/stores`), Spring Boot Admin, Springdoc
- **benchmark** — JMH benchmarks comparing the 4 batch insert strategies

## Prerequisites

- Java 17+
- Docker (for PostgreSQL)

## Getting Started

```bash
# Start PostgreSQL
docker compose up -d

# Build
./gradlew clean build -x test

# Run the application
./gradlew :web:bootRun
```

The application starts on port `8081`.

## API

```bash
# Batch insert (modes: jdbc, hibernate, unnest, copy)
curl -X POST "http://localhost:8081/customers/save-all?number=1000&mode=copy"

# Get all customers
curl http://localhost:8081/customers/get-all

# Get all with store and orders (eager fetch)
curl http://localhost:8081/customers/get-all-with-store-orders

# Delete all
curl -X DELETE http://localhost:8081/customers/delete-all
```

## Tests

```bash
# Unit tests (H2)
./gradlew :web:test

# Integration tests (requires PostgreSQL)
./gradlew :batch-core:integrationTest
```

## JMH Benchmarks

```bash
# Run benchmarks (requires PostgreSQL)
./gradlew :benchmark:jmh
```

Results are written to `benchmark/build/reports/jmh/results.json`.

### Results (1,000 customers + 1,000 orders per batch)

> Environment: JDK 17.0.18 (OpenJDK), PostgreSQL 17 (Docker), `batch_size=100`, `reWriteBatchedInserts=true`
>
> JMH config: fork=1, warmup=1x3s, measurement=3x5s, mode=AverageTime

| Strategy | Avg (ms/op) | Error (ms) |
|----------|-------------|------------|
| UNNEST | **21.17** | ± 33.61 |
| JDBC Batch | 26.21 | ± 63.64 |
| COPY | 26.85 | ± 27.19 |
| Hibernate Batch | 168.24 | ± 800.00 |

**UNNEST** and **JDBC Batch** are the fastest for 1,000 rows. **Hibernate Batch** is significantly slower due to the persistence context overhead (dirty checking, cascade).

## Tech Stack

- Spring Boot 3.2.2
- Spring Data JPA / Hibernate 6.4
- PostgreSQL 17
- Lombok
- JMH 1.37
- Gradle 8.5 (multi-module)
