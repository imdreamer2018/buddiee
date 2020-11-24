package com.thoughtworks.buddiee.serviceTests;

import com.thoughtworks.buddiee.dto.Page;
import com.thoughtworks.buddiee.dto.Product;
import com.thoughtworks.buddiee.entity.ProductEntity;
import com.thoughtworks.buddiee.exception.ResourceNotFoundException;
import com.thoughtworks.buddiee.repository.ProductRepository;
import com.thoughtworks.buddiee.service.ProductService;
import com.thoughtworks.buddiee.util.AliyunOssUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.thoughtworks.buddiee.service.ProductService.CAN_NOT_FIND_BASIC_INFO_OF_PRODUCT_WITH_ID_IS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    private ProductService productService;

    @Mock
    ProductRepository productRepository;

    @Mock
    AliyunOssUtil aliyunOssUtil;

    private Product product;

    private ProductEntity productEntity;

    private List<Product> products = new ArrayList<>();

    private List<ProductEntity> productEntities = new ArrayList<>();

    @BeforeEach
    void setUp() {
        initMocks(this);
        productService = new ProductService(productRepository, aliyunOssUtil);
        product = Product.builder()
                .name("mock product name")
                .description("mock product description")
                .imageUrl("mock image url.com/path")
                .price(new BigDecimal(10))
                .build();
        productEntity = product.toProductEntity();
        products.add(product);
        productEntities.add(productEntity);

    }

    @Nested
    class CreateProduct {

        @Nested
        class WhenCreateSuccess {

            @Test
            void should_return_product_info() throws IOException {
                when(aliyunOssUtil.uploadBase64FileToAliyunOss(anyString(), anyString())).thenReturn(product.getImageUrl());
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
                when(productRepository.findById(anyLong())).thenReturn(Optional.of(productEntity));
                doNothing().when(aliyunOssUtil).deleteFile(anyString());
                productService.deleteProduct(1L);
                verify(productRepository, times(1)).deleteById(1L);
            }
        }

        @Nested
        class WhenProductIdIsNotExisted {

            @Test
            void should_throw_resource_not_found_exception() {
                when(productRepository.findById(1L)).thenReturn(Optional.empty());
                ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> productService.deleteProduct(1L));
                assertEquals(CAN_NOT_FIND_BASIC_INFO_OF_PRODUCT_WITH_ID_IS + 1L, exception.getMessage());
            }
        }
    }

    @Nested
    class FindProduct {

        @Nested
        class WhenProductIdIsExisted {

            @Test
            void should_return_product_info() {
                when(productRepository.findById(1L)).thenReturn(Optional.of(productEntity));
                Product product = productService.findProduct(1L);
                assertEquals("mock product name", product.getName());
            }
        }

        @Nested
        class WhenProductIdIsNotExisted {

            @Test
            void should_throw_resource_not_found_exception() {
                when(productRepository.findById(1L)).thenReturn(Optional.empty());
                ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> productService.findProduct(1L));
                assertEquals(CAN_NOT_FIND_BASIC_INFO_OF_PRODUCT_WITH_ID_IS + 1L, exception.getMessage());
            }
        }
    }

    @Nested
    class UpdateProduct {

        @Nested
        class WhenProductIdIsExisted {

            @Test
            void should_return_product_info() {
                when(productRepository.findById(1L)).thenReturn(Optional.of(productEntity));
                Product updateProduct = productService.updateProduct(1L, product);
                assertEquals("mock product name", updateProduct.getName());
            }
        }

        @Nested
        class WhenProductIdIsNotExisted {

            @Test
            void should_throw_resource_not_found_exception() {
                when(productRepository.findById(1L)).thenReturn(Optional.empty());
                ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> productService.updateProduct(1L, product));
                assertEquals(CAN_NOT_FIND_BASIC_INFO_OF_PRODUCT_WITH_ID_IS + 1L, exception.getMessage());
            }
        }
    }

    @Nested
    class FindProducts {

        @Test
        void should_return_products_info() {
            Pageable pageable = PageRequest.of(0, 1);
            org.springframework.data.domain.Page<ProductEntity> productsPage = new PageImpl<>(productEntities);
            when(productRepository.findAll(pageable)).thenReturn(productsPage);
            Page<Product> products = productService.findProducts(1, 1);
            assertEquals(1, products.getData().size());
        }
    }

}
