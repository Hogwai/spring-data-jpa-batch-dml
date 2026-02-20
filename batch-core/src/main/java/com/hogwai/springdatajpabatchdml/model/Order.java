package com.hogwai.springdatajpabatchdml.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * JPA entity representing an order.
 * Mapped to the {@code orders} table to avoid conflict with the SQL reserved word {@code ORDER}.
 * Each order belongs to a {@link Customer}.
 */
@Entity(name = "orders")
@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_seq")
    @SequenceGenerator(name = "order_seq", sequenceName = "order_seq", allocationSize = 50)
    private Long id;

    private String content;
    private Date orderDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
}
