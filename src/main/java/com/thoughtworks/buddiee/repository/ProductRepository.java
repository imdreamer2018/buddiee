package com.thoughtworks.buddiee.repository;

import com.thoughtworks.buddiee.dto.Product;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ProductRepository {

    private final Map<Long, Product> productMap = new HashMap<>();

    private final AtomicLong maxProductId = new AtomicLong(0L);

    public Long incrementProductId() {
        return maxProductId.incrementAndGet();
    }

    public void deleteAll() {
        productMap.clear();
    }

    public Long save(Product product) {
        productMap.put(incrementProductId(), product);
        return maxProductId.get();
    }

    public Optional<Product> findById(Long id) {
        return Optional.ofNullable(productMap.get(id));
    }

    public void deleteById(Long id) {
        productMap.remove(id);
    }
}
