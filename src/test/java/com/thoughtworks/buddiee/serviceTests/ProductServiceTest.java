package com.thoughtworks.buddiee.serviceTests;

import com.thoughtworks.buddiee.dto.Product;
import com.thoughtworks.buddiee.exception.BadRequestException;
import com.thoughtworks.buddiee.repository.ProductRepository;
import com.thoughtworks.buddiee.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
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

    @Nested
    class DeleteProduct {

        @Nested
        class WhenProductIdIsExisted {

            @Test
            void should_delete_success() {
                when(productRepository.findById(1L)).thenReturn(Optional.of(product));
                productService.deleteProduct(1L);
                verify(productRepository).deleteById(1L);
            }
        }

        @Nested
        class WhenProductIdIsNotExisted {

            @Test
            void should_throw_exception() {
                when(productRepository.findById(1L)).thenReturn(Optional.empty());
                BadRequestException exception = assertThrows(BadRequestException.class, () -> productService.deleteProduct(1L));
                assertEquals("can not find basic info of product with id is " + 1L, exception.getMessage());
            }
        }
    }

    @Nested
    class FindProduct {

        @Nested
        class WhenProductIdIsExisted {

            @Test
            void should_return_product_info() {
                when(productRepository.findById(1L)).thenReturn(Optional.of(product));
                Product product = productService.findProduct(1L);
                assertEquals("mock product name", product.getName());
            }
        }

        @Nested
        class WhenProductIdIsNotExisted {

            @Test
            void should_throw_bad_request_exception() {
                when(productRepository.findById(1L)).thenReturn(Optional.empty());
                BadRequestException exception = assertThrows(BadRequestException.class, () -> productService.findProduct(1L));
                assertEquals("can not find basic info of product with id is " + 1L, exception.getMessage());
            }
        }
    }

}
