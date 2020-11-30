package com.thoughtworks.buddiee.serviceTests;

import com.thoughtworks.buddiee.dto.Page;
import com.thoughtworks.buddiee.dto.ProductDTO;
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
import org.springframework.data.domain.Sort;

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
class ProductServiceTest {

    public static final String MOCK_PRODUCT_NAME = "mock product name";
    private static ProductService productService;

    @Mock
    static ProductRepository productRepository;

    @Mock
    static AliyunOssUtil aliyunOssUtil;

    private static ProductDTO productDTO;

    private static ProductEntity productEntity;

    private static final List<ProductEntity> productEntities = new ArrayList<>();

    @BeforeEach
    void setUp() {
        initMocks(this);
        productService = new ProductService(productRepository, aliyunOssUtil);
        productDTO = ProductDTO.builder()
                .name(MOCK_PRODUCT_NAME)
                .description("mock product description")
                .imageUrl("mock image url.com/path")
                .price(new BigDecimal(10))
                .build();
        productEntity = productDTO.toProductEntity();
        productEntities.add(productEntity);

    }

    @Nested
    class CreateProductDTO {

        @Nested
        class WhenCreateSuccess {

            @Test
            void should_return_product_info() throws IOException {
                when(aliyunOssUtil.uploadBase64FileToAliyunOss(anyString(), anyString())).thenReturn(productDTO.getImageUrl());
                ProductDTO productDTOResponse = productService.createProduct(productDTO);
                assertEquals(MOCK_PRODUCT_NAME, productDTOResponse.getName());
            }
        }
    }

    @Nested
    class DeleteProductDTO {

        @Nested
        class WhenProductDTOIdIsExisted {

            @Test
            void should_delete_success() {
                when(productRepository.findById(anyLong())).thenReturn(Optional.of(productEntity));
                doNothing().when(aliyunOssUtil).deleteFile(anyString());
                productService.deleteProduct(1L);
                verify(productRepository, times(1)).deleteById(1L);
            }
        }

        @Nested
        class WhenProductDTOIdIsNotExisted {

            @Test
            void should_throw_resource_not_found_exception() {
                when(productRepository.findById(1L)).thenReturn(Optional.empty());
                ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> productService.deleteProduct(1L));
                assertEquals(CAN_NOT_FIND_BASIC_INFO_OF_PRODUCT_WITH_ID_IS + 1L, exception.getMessage());
            }
        }
    }

    @Nested
    class FindProductDTO {

        @Nested
        class WhenProductDTOIdIsExisted {

            @Test
            void should_return_product_info() {
                when(productRepository.findById(1L)).thenReturn(Optional.of(productEntity));
                ProductDTO productDTO = productService.findProduct(1L);
                assertEquals(MOCK_PRODUCT_NAME, productDTO.getName());
            }
        }

        @Nested
        class WhenProductDTOIdIsNotExisted {

            @Test
            void should_throw_resource_not_found_exception() {
                when(productRepository.findById(1L)).thenReturn(Optional.empty());
                ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> productService.findProduct(1L));
                assertEquals(CAN_NOT_FIND_BASIC_INFO_OF_PRODUCT_WITH_ID_IS + 1L, exception.getMessage());
            }
        }
    }

    @Nested
    class UpdateProductDTO {

        @Nested
        class WhenProductDTOIdIsExisted {

            @Test
            void should_return_product_info() {
                when(productRepository.findById(1L)).thenReturn(Optional.of(productEntity));
                ProductDTO updateProductDTO = productService.updateProduct(1L, productDTO);
                assertEquals(MOCK_PRODUCT_NAME, updateProductDTO.getName());
            }
        }

        @Nested
        class WhenProductDTOIdIsNotExisted {

            @Test
            void should_throw_resource_not_found_exception() {
                when(productRepository.findById(1L)).thenReturn(Optional.empty());
                ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> productService.updateProduct(1L, productDTO));
                assertEquals(CAN_NOT_FIND_BASIC_INFO_OF_PRODUCT_WITH_ID_IS + 1L, exception.getMessage());
            }
        }
    }

    @Nested
    class FindProducts {

        @Test
        void should_return_products_info() {
            Pageable pageable = PageRequest.of(0, 1, Sort.by("id").descending());
            org.springframework.data.domain.Page<ProductEntity> productsPage = new PageImpl<>(productEntities);
            when(productRepository.findAll(pageable)).thenReturn(productsPage);
            Page<ProductDTO> products = productService.findProducts(1, 1);
            assertEquals(1, products.getData().size());
        }
    }

}
