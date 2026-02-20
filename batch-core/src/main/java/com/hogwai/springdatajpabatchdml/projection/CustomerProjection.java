package com.hogwai.springdatajpabatchdml.projection;

import java.util.List;

/**
 * Spring Data projection for {@link com.hogwai.springdatajpabatchdml.model.Customer},
 * exposing only the name, store and orders without loading the full entity graph.
 */
public interface CustomerProjection {
    String getFirstName();
    String getLastName();
    StoreProjection getStore();
    List<OrderProjection> getOrders();
}
