package com.hogwai.springdatajpabatchdml.controller;

import com.hogwai.springdatajpabatchdml.model.Store;
import com.hogwai.springdatajpabatchdml.service.StoreService;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stores")
public class StoreController {
    private final StoreService storeService;

    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @PostMapping("/save-all")
    public ResponseEntity<String> insertStores(@RequestParam Integer number) {
        StopWatch watch = new StopWatch();
        watch.start();
        storeService.saveAll(number);
        watch.stop();
        System.out.println("Time elapsed for insert: " + watch.getTotalTimeSeconds());
        return ResponseEntity.ok("Inserted in " + watch.getTotalTimeSeconds());
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<Store>> getAllStores() {
        StopWatch watch = new StopWatch();
        watch.start();
        List<Store> stores = storeService.getAll();
        watch.stop();
        System.out.printf("Total time elapsed for getting all stores: %.0f %n", watch.getTotalTimeSeconds());
        return ResponseEntity.ok(stores);
    }

    @DeleteMapping("/delete-all")
    public ResponseEntity<String> deleteAllStores() {
        StopWatch watch = new StopWatch();
        watch.start();
        storeService.deleteAll();
        watch.stop();
        System.out.printf("Total time elapsed for deleting all stores: %.0f %n", watch.getTotalTimeSeconds());
        return ResponseEntity.ok().body("Deleted all stores");
    }
}
