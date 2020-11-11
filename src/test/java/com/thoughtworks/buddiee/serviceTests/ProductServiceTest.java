package com.thoughtworks.buddiee.serviceTests;

import com.thoughtworks.buddiee.repository.ProductRepository;
import com.thoughtworks.buddiee.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.MockitoAnnotations.initMocks;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    private ProductService productService;

    @Mock
    ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        initMocks(this);
        productService = new ProductService(productRepository);
    }

}
