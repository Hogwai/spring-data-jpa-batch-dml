package com.hogwai.springdatajpabatchdml.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

/**
 * JPA entity representing a customer.
 * Each customer belongs to a {@link Store} and has one or more {@link Order}s.
 * Uses a database sequence ({@code cust_seq}) with an allocation size of 50 for ID generation.
 */
@Entity
@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
@Setter
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cust_seq")
    @SequenceGenerator(name = "cust_seq", sequenceName = "cust_seq", allocationSize = 50)
    private Long id;
    private String firstName;
    private String lastName;
    private String country;
    private String address;
    private String city;
    private Date creationDate;
    private Date updateDate;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customer")
    private List<Order> orders;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;
}