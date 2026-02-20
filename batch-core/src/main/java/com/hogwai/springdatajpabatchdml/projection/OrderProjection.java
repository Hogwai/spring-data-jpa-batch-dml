package com.hogwai.springdatajpabatchdml.projection;

import java.util.Date;

/**
 * Spring Data projection for {@link com.hogwai.springdatajpabatchdml.model.Order},
 * exposing only content and order date.
 */
public interface OrderProjection {
    String getContent();
    Date getOrderDate();
}
