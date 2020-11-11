package com.thoughtworks.buddiee.repository;

import com.thoughtworks.buddiee.dto.Product;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ProductRepository {

    private final Map<Long, Product> productMap = new HashMap<>();

    private final AtomicLong maxProductId = new AtomicLong(1L);

    public Long getMaxProductId() {
        return maxProductId.get();
    }

    public void incrementProductId() {
        maxProductId.incrementAndGet();
    }

    public void deleteAll() {
        productMap.clear();
    }

    public void save(Product product) {
        productMap.put(maxProductId.get(), product);
    }
}
