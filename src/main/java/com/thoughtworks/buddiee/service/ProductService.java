package com.thoughtworks.buddiee.service;

import com.thoughtworks.buddiee.dto.Product;
import com.thoughtworks.buddiee.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product createProduct(Product product) {
        Long productId = productRepository.save(product);
        product.setId(productId);
        return product;
    }
}
