package com.hogwai.springdatajpabatchdml.projection;

/**
 * Spring Data projection for {@link com.hogwai.springdatajpabatchdml.model.Store},
 * exposing only the store name.
 */
public interface StoreProjection {
    String getStoreName();
}