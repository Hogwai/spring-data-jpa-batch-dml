package com.hogwai.springdatajpabatchdml.service;

import com.hogwai.springdatajpabatchdml.model.Store;

import java.util.List;

public interface StoreService {
    void saveAll(Integer number);

    List<Store> getAll();

    void deleteAll();
}
