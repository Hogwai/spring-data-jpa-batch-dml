package com.hogwai.springdatajpabatchdml.service.impl;

import com.hogwai.springdatajpabatchdml.model.Store;
import com.hogwai.springdatajpabatchdml.repository.StoreRepository;
import com.hogwai.springdatajpabatchdml.service.StoreService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.hogwai.springdatajpabatchdml.util.StoreFactory.generateStores;

@Service
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;

    public StoreServiceImpl(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    @Override
    @Transactional
    public void saveAll(Integer number) {
        List<Store> stores = generateStores(number);
        System.out.println("Generated Stores: " + stores.size());
        storeRepository.mergeAll(stores);
    }

    @Override
    @Transactional
    public List<Store> getAll() {
        List<Store> stores = storeRepository.findAll();
        System.out.println("Found stores: " + stores.size());
        return stores;
    }

    @Override
    @Transactional
    public void deleteAll() {
        storeRepository.deleteAll();
    }
}
