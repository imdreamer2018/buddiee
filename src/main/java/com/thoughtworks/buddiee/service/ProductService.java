package com.thoughtworks.buddiee.service;

import com.thoughtworks.buddiee.dto.Page;
import com.thoughtworks.buddiee.dto.Product;
import com.thoughtworks.buddiee.entity.ProductEntity;
import com.thoughtworks.buddiee.exception.ResourceNotFoundException;
import com.thoughtworks.buddiee.repository.ProductRepository;
import com.thoughtworks.buddiee.util.AliyunOssUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProductService {

    public static final String CAN_NOT_FIND_BASIC_INFO_OF_PRODUCT_WITH_ID_IS = "can not find basic info of product with id is ";
    private final ProductRepository productRepository;
    private final AliyunOssUtil aliyunOssUtil;

    public Product createProduct(Product product) throws IOException {
        String url = aliyunOssUtil.uploadBase64FileToAliyunOss("image/product/", product.getImageUrl());
        product.setImageUrl(url);
        ProductEntity productEntity = product.toProductEntity();
        productRepository.save(productEntity);
        return productEntity.toProduct();
    }

    public void deleteProduct(Long productId) {
        ProductEntity productEntity = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(CAN_NOT_FIND_BASIC_INFO_OF_PRODUCT_WITH_ID_IS + productId));
        String productImageAbsolutePath = productEntity.getImageUrl().split("com/")[1];
        aliyunOssUtil.deleteFile(productImageAbsolutePath);
        productRepository.deleteById(productId);
    }

    public Product findProduct(Long productId) {
        ProductEntity productEntity = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(CAN_NOT_FIND_BASIC_INFO_OF_PRODUCT_WITH_ID_IS + productId));
        return productEntity.toProduct();
    }

    public Product updateProduct(Long productId, Product updateProduct) {
        ProductEntity productEntity = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(CAN_NOT_FIND_BASIC_INFO_OF_PRODUCT_WITH_ID_IS + productId));
        updateProduct.setId(productEntity.getId());
        productRepository.save(updateProduct.toProductEntity());
        return updateProduct;
    }

    public Page<Product> findProducts(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by("id").descending());
        org.springframework.data.domain.Page<ProductEntity> productEntities = productRepository.findAll(pageable);
        List<Product> products = productEntities.stream().map(ProductEntity::toProduct).collect(Collectors.toList());
        Page<Product> page = new Page<>();
        page.setCurrentPage(pageNumber);
        page.setTotalPage(productEntities.getTotalPages());
        page.setData(products);
        return page;
    }
}
