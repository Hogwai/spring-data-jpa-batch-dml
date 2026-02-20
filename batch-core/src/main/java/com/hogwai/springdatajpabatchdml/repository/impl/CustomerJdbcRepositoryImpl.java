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
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import org.postgresql.copy.*;

/**
 * PostgreSQL-specific implementation of {@link CustomerJdbcRepository}.
 *
 * <p>Provides two bulk insert strategies:</p>
 * <ul>
 *   <li><b>UNNEST</b> — passes each column as a PostgreSQL array and inserts
 *       all rows in a single {@code INSERT ... SELECT FROM UNNEST(...)} statement.</li>
 *   <li><b>COPY</b> — writes tab-separated values through the PostgreSQL
 *       {@link org.postgresql.copy.CopyManager} for maximum throughput.</li>
 *   <li><b>Multi-row VALUES</b> — builds a single {@code INSERT INTO ... VALUES (...), (...)}
 *       statement with all rows inlined as parameter placeholders.</li>
 * </ul>
 */
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
                    SELECT nextval('cust_seq'), * FROM UNNEST(?, ?, ?, ?, ?, ?, ?)
                """;

        List<List<Customer>> batches = splitIntoBatches(customers, BATCH_SIZE);

        for (List<Customer> batch : batches) {
            jdbcTemplate.execute((Connection conn) -> {
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setArray(1, conn.createArrayOf("text", extractArray(batch, Customer::getFirstName)));
                    ps.setArray(2, conn.createArrayOf("text", extractArray(batch, Customer::getLastName)));
                    ps.setArray(3, conn.createArrayOf("text", extractArray(batch, Customer::getAddress)));
                    ps.setArray(4, conn.createArrayOf("text", extractArray(batch, Customer::getCity)));
                    ps.setArray(5, conn.createArrayOf("text", extractArray(batch, Customer::getCountry)));
                    ps.setArray(6, conn.createArrayOf("date", extractArray(batch, Customer::getCreationDate)));
                    ps.setArray(7, conn.createArrayOf("bigint", extractArray(batch, c -> c.getStore().getId())));
                    return ps.executeUpdate();
                }
            });
        }
    }

    @Override
    public void insertWithCopy(List<Customer> customers) {
        // Pre-allocate IDs from the sequence
        List<Long> ids = jdbcTemplate.queryForList(
                "SELECT nextval('cust_seq') FROM generate_series(1, ?)",
                Long.class, customers.size());
        for (int i = 0; i < customers.size(); i++) {
            customers.get(i).setId(ids.get(i));
        }

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

    @Override
    public void insertWithMultiRowValues(List<Customer> customers) {
        List<List<Customer>> batches = splitIntoBatches(customers, BATCH_SIZE);

        for (List<Customer> batch : batches) {
            StringBuilder sql = new StringBuilder(
                    "INSERT INTO customer (id, first_name, last_name, address, city, country, creation_date, store_id) VALUES ");

            for (int i = 0; i < batch.size(); i++) {
                if (i > 0) sql.append(", ");
                sql.append("(nextval('cust_seq'), ?, ?, ?, ?, ?, ?, ?)");
            }

            jdbcTemplate.update(sql.toString(), ps -> {
                int index = 1;
                for (Customer customer : batch) {
                    ps.setString(index++, customer.getFirstName());
                    ps.setString(index++, customer.getLastName());
                    ps.setString(index++, customer.getAddress());
                    ps.setString(index++, customer.getCity());
                    ps.setString(index++, customer.getCountry());
                    ps.setDate(index++, new java.sql.Date(customer.getCreationDate().getTime()));
                    ps.setLong(index++, customer.getStore().getId());
                }
            });
        }
    }

    private <T> Object[] extractArray(List<Customer> batch, Function<Customer, T> extractor) {
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
