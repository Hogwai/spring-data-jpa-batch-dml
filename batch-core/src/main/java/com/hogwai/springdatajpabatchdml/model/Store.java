package com.hogwai.springdatajpabatchdml.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * JPA entity representing a store.
 * A store has many {@link Customer}s, cascading persist operations.
 */
@Entity
@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "store_seq")
    @SequenceGenerator(name = "store_seq", sequenceName = "store_seq", allocationSize = 50)
    private Long id;

    private String storeName;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "store")
    private List<Customer> customers;
}
