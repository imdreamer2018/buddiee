package com.thoughtworks.buddiee.service;

import com.thoughtworks.buddiee.dto.Page;
import com.thoughtworks.buddiee.dto.Product;
import com.thoughtworks.buddiee.exception.ResourceNotFoundException;
import com.thoughtworks.buddiee.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    public static final String CAN_NOT_FIND_BASIC_INFO_OF_PRODUCT_WITH_ID_IS = "can not find basic info of product with id is ";
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product createProduct(Product product) {
        productRepository.save(product);
        return product;
    }

    public void deleteProduct(Long productId) {
        if (!productRepository.findById(productId).isPresent())
            throw new ResourceNotFoundException(CAN_NOT_FIND_BASIC_INFO_OF_PRODUCT_WITH_ID_IS + productId);
        productRepository.deleteById(productId);
    }

    public Product findProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(CAN_NOT_FIND_BASIC_INFO_OF_PRODUCT_WITH_ID_IS + productId));
    }

    public Product updateProduct(Long productId, Product updateProduct) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(CAN_NOT_FIND_BASIC_INFO_OF_PRODUCT_WITH_ID_IS + productId));
        updateProduct.setId(product.getId());
        productRepository.save(updateProduct);
        return updateProduct;
    }

    public Page<Product> findProducts(int pageNumber, int pageSize) {
        return productRepository.findAll(pageNumber, pageSize);
    }
}
