package com.thoughtworks.buddiee.controllerTests;

import com.thoughtworks.buddiee.controller.ProductController;
import com.thoughtworks.buddiee.dto.Product;
import com.thoughtworks.buddiee.service.ProductService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
@AutoConfigureJsonTesters
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private JacksonTester<Product> productJson;

    private Product product;

    @BeforeEach
    void setUp() {
        product = Product.builder()
                .name("mock product name")
                .description("mock product description")
                .imageUrl("mock image url")
                .price(10)
                .build();
    }

    @AfterEach
    void afterEach() {
        Mockito.reset(productService);
    }

    @Nested
    class CreateProduct {

        @Nested
        class WhenCreateSuccess {

            @Test
            void should_return_product_info() throws Exception {
                when(productService.createProduct(product)).thenReturn(product);
                mockMvc.perform(post("/products")
                        .content(productJson.write(product).getJson())
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$.name", is("mock product name")));
                verify(productService).createProduct(product);
            }
        }
    }
}
