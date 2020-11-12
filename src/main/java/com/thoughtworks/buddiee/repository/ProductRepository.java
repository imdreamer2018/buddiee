package com.thoughtworks.buddiee.repository;

import com.thoughtworks.buddiee.dto.Page;
import com.thoughtworks.buddiee.dto.Product;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ProductRepository {

    private final Map<Long, Product> productMap = new HashMap<>();

    private final AtomicLong maxProductId = new AtomicLong(0L);

    public void deleteAll() {
        productMap.clear();
    }

    public Long save(Product product) {
        if (product.getId() != null)
            productMap.put(product.getId(), product);
        else {
            productMap.put(maxProductId.incrementAndGet(), product);
            product.setId(maxProductId.get());
        }
        return product.getId();
    }

    public Optional<Product> findById(Long id) {
        return Optional.ofNullable(productMap.get(id));
    }

    public void deleteById(Long id) {
        productMap.remove(id);
    }

    private Page<Product> getReverseSortPageProducts(int pageNumber, int totalPage, int startKey, int endKey ) {
        List<Product> allProducts = new ArrayList<>(productMap.values());
        Collections.reverse(allProducts);
        Page<Product> result = new Page<>();
        result.setCurrentPage(pageNumber);
        result.setTotalPage(totalPage);
        result.setData(allProducts.subList(startKey, endKey));
        return result;
    }

    public Page<Product> findAll(int pageNumber, int pageSize) {
        int pages = productMap.size() % pageSize != 0 ? productMap.size() / pageSize + 1 : productMap.size() / pageSize;
        if (pageNumber > pages)
            return getReverseSortPageProducts(pageNumber, pages, 0, 0);

        if (pageNumber == pages)
            return getReverseSortPageProducts(pageNumber, pages, (pageNumber - 1) * pageSize, productMap.size());

        return getReverseSortPageProducts(pageNumber, pages, (pageNumber - 1) * pageSize, pageNumber * pageSize);
    }
}
