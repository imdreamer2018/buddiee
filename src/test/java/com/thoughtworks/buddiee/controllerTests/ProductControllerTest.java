package com.thoughtworks.buddiee.controllerTests;

import com.thoughtworks.buddiee.controller.ProductController;
import com.thoughtworks.buddiee.dto.Product;
import com.thoughtworks.buddiee.service.ProductService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ProductController.class)
@AutoConfigureJsonTesters
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private JacksonTester<Product> productJson;

    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void afterEach() {
        Mockito.reset(productService);
    }
}
