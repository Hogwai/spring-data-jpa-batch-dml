package com.hogwai.springdatajpabatchdml.service.impl;

import com.hogwai.springdatajpabatchdml.model.Store;
import com.hogwai.springdatajpabatchdml.repository.StoreRepository;
import com.hogwai.springdatajpabatchdml.service.StoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.hogwai.springdatajpabatchdml.util.StoreFactory.generateStores;
import static com.hogwai.springdatajpabatchdml.util.StringUtil.generateRandomString;

/**
 * Default implementation of {@link StoreService}.
 *
 * <p>Delegates persistence to {@link StoreRepository} using the custom
 * {@code persist}/{@code persistAll} operations provided by {@link
 * com.hogwai.springdatajpabatchdml.repository.HibernateRepository}.</p>
 */
@Service
public class StoreServiceImpl implements StoreService {

    private static final Logger log = LoggerFactory.getLogger(StoreServiceImpl.class);

    private final StoreRepository storeRepository;

    public StoreServiceImpl(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    @Override
    @Transactional
    public void saveAll(Integer number) {
        List<Store> stores = generateStores(number);
        log.info("Generated Stores: {}", stores.size());
        storeRepository.persistAll(stores);
    }

    @Transactional
    @Override
    public void saveOne() {
        Store store = Store.builder()
                .storeName(generateRandomString())
                .build();
        storeRepository.persist(store);
    }

    @Override
    @Transactional
    public List<Store> getAll() {
        List<Store> stores = storeRepository.findAll();
        log.info("Found stores: {}", stores.size());
        return stores;
    }

    @Override
    @Transactional
    public void deleteAll() {
        storeRepository.deleteAll();
    }
}
