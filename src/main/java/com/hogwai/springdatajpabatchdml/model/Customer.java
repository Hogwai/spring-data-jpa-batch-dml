package com.hogwai.springdatajpabatchdml.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
@Setter
@NamedEntityGraph(
        name = "customer-with-orders",
        attributeNodes = {
                @NamedAttributeNode("orders")
        }
)
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cust_seq")
    @SequenceGenerator(name = "cust_seq", sequenceName = "cust_seq", allocationSize = 1)
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