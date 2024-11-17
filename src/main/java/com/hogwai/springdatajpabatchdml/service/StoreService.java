package com.hogwai.springdatajpabatchdml.service;

import com.hogwai.springdatajpabatchdml.model.Store;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface StoreService {
    void saveAll(Integer number);

    @Transactional
    void saveOne(Long storeId);

    List<Store> getAll();

    void deleteAll();
}
