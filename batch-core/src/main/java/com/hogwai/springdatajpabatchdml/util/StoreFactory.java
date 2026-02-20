package com.hogwai.springdatajpabatchdml.util;

import com.hogwai.springdatajpabatchdml.model.Store;

import java.util.ArrayList;
import java.util.List;

import static com.hogwai.springdatajpabatchdml.util.StringUtil.generateRandomString;

public class StoreFactory {

    private StoreFactory() {
    }

    public static List<Store> generateStores(Integer number) {
        List<Store> stores = new ArrayList<>(number);
        for (int i = 0; i < number; i++) {
            Store store = Store.builder()
                    .storeName(generateRandomString())
                    .build();
            stores.add(store);
        }
        return stores;
    }
}
