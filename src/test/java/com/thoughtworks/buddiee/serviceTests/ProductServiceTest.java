package com.thoughtworks.buddiee.serviceTests;

import com.thoughtworks.buddiee.dto.Product;
import com.thoughtworks.buddiee.repository.ProductRepository;
import com.thoughtworks.buddiee.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    private ProductService productService;

    @Mock
    ProductRepository productRepository;

    private Product product;

    @BeforeEach
    void setUp() {
        initMocks(this);
        productService = new ProductService(productRepository);
        product = Product.builder()
                .name("mock product name")
                .description("mock product description")
                .imageUrl("mock image url")
                .price(10)
                .build();
    }

    @Nested
    class CreateProduct {

        @Nested
        class WhenCreateSuccess {

            @Test
            void should_return_product_info() {
                Product productResponse = productService.createProduct(product);
                assertEquals("mock product name", productResponse.getName());
            }
        }
    }

}
