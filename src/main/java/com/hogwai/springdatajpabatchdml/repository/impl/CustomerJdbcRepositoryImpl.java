package com.hogwai.springdatajpabatchdml.repository.impl;

import com.hogwai.springdatajpabatchdml.model.Customer;
import com.hogwai.springdatajpabatchdml.repository.CustomerJdbcRepository;
import org.postgresql.PGConnection;
import org.postgresql.core.BaseConnection;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.StringReader;
import java.io.StringWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import org.postgresql.copy.*;

@Repository
public class CustomerJdbcRepositoryImpl implements CustomerJdbcRepository {

    private static final int BATCH_SIZE = 10000;
    private final JdbcTemplate jdbcTemplate;

    public CustomerJdbcRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public void batchInsertWithArrays(List<Customer> customers) {
        String sql = """
                    INSERT INTO customer (
                        id, first_name, last_name, address, city,
                        country, creation_date, store_id
                    )
                    SELECT * FROM UNNEST(
                        ?::bigint[],
                        ?::text[],
                        ?::text[],
                        ?::text[],
                        ?::text[],
                        ?::text[],
                        ?::date[],
                        ?::bigint[]
                    )
                """;

        // Préparation des tableaux
        List<List<Customer>> batches = splitIntoBatches(customers, BATCH_SIZE);

        for (List<Customer> batch : batches) {
            Object[] params = {
                    createSqlArray(batch, Customer::getId),
                    createSqlArray(batch, Customer::getFirstName),
                    createSqlArray(batch, Customer::getLastName),
                    createSqlArray(batch, Customer::getAddress),
                    createSqlArray(batch, Customer::getCity),
                    createSqlArray(batch, Customer::getCountry),
                    createSqlArray(batch, Customer::getCreationDate),
                    createSqlArray(batch, customer -> customer.getStore().getId())
            };

            jdbcTemplate.update(sql, params);
        }
    }

    // Alternative avec copy
    @Override
    public void insertWithCopy(List<Customer> customers) {
        String sql = """
                    COPY customer (
                        id, first_name, last_name, address, city,
                        country, creation_date, store_id
                    ) FROM STDIN
                """;

        try (Connection connection = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection()) {

            PGConnection pgConnection = connection.unwrap(PGConnection.class);
            CopyManager copyManager = new CopyManager((BaseConnection) pgConnection);

            StringWriter writer = new StringWriter();
            for (Customer customer : customers) {
                writer.write(String.format("%d\t%s\t%s\t%s\t%s\t%s\t%s\t%d%n",
                        customer.getId(),
                        escapeCopyValue(customer.getFirstName()),
                        escapeCopyValue(customer.getLastName()),
                        escapeCopyValue(customer.getAddress()),
                        escapeCopyValue(customer.getCity()),
                        escapeCopyValue(customer.getCountry()),
                        customer.getCreationDate(),
                        customer.getStore().getId()
                ));
            }

            copyManager.copyIn(sql, new StringReader(writer.toString()));

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'insertion COPY", e);
        }
    }

    // Méthodes utilitaires
    private <T> Object createSqlArray(List<Customer> batch, Function<Customer, T> extractor) {
        return batch.stream()
                .map(extractor)
                .toArray();
    }

    private List<List<Customer>> splitIntoBatches(List<Customer> items, int batchSize) {
        List<List<Customer>> batches = new ArrayList<>();
        for (int i = 0; i < items.size(); i += batchSize) {
            batches.add(items.subList(i, Math.min(i + batchSize, items.size())));
        }
        return batches;
    }

    private String escapeCopyValue(String value) {
        if (value == null) return "\\N";
        return value.replace("\t", "\\t")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }
}
