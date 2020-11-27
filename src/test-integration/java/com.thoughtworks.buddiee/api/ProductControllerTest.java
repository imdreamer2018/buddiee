package com.thoughtworks.buddiee.api;

import com.thoughtworks.buddiee.base.ApiBaseTest;
import com.thoughtworks.buddiee.dto.Product;
import com.thoughtworks.buddiee.entity.ProductEntity;
import com.thoughtworks.buddiee.repository.ProductRepository;
import com.thoughtworks.buddiee.util.AliyunOssUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.math.BigDecimal;

import static com.thoughtworks.buddiee.service.ProductService.CAN_NOT_FIND_BASIC_INFO_OF_PRODUCT_WITH_ID_IS;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;


class ProductControllerTest extends ApiBaseTest {

    @Autowired
    ProductRepository productRepository;

    @Mock
    AliyunOssUtil aliyunOssUtil;


    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
    }

    @Test
    void should_return_product_info_when_find_product_existed() {
        productRepository.save(
                ProductEntity.builder()
                        .name("可乐")
                        .description("快乐水")
                        .imageUrl("http://####")
                        .price(new BigDecimal("2.5"))
                        .build()
        );
        givenMockMvc()
                .get("/products/{id}", 1)
                .then()
                .statusCode(200)
                .body("name", is("可乐"));

    }

    @Test
    void should_throw_exception_when_find_product_not_existed() {
        givenMockMvc()
                .get("/products/{id}", 1)
                .then()
                .statusCode(404)
                .body("message", is(CAN_NOT_FIND_BASIC_INFO_OF_PRODUCT_WITH_ID_IS + "1"));
    }



    @Test
    void should_return_product_info_when_create_product_success() throws IOException {
        Product productRequest = Product.builder()
                .name("元气森林")
                .description("无糖饮料，好喝！")
                .imageUrl("data:img/jpg;base64,iVBORw0KGgo")
                .price(new BigDecimal("4"))
                .build();

        when(aliyunOssUtil.uploadBase64FileToAliyunOss(anyString(), anyString())).thenReturn("mock url");

        givenMockMvc()
                .body(productRequest)
                .post("/products")
                .then()
                .statusCode(201)
                .body("name", is("元气森林"));

    }

    @Test
    void should_delete_success_when_delete_product_existed() {
        productRepository.save(
                ProductEntity.builder()
                        .name("可乐")
                        .description("快乐水")
                        .imageUrl("http://####.com/image")
                        .price(new BigDecimal("2.5"))
                        .build()
        );
        doNothing().when(aliyunOssUtil).deleteFile(anyString());
        givenMockMvc()
                .delete("/products/{id}", 1)
                .then()
                .statusCode(204);
    }

    @Test
    void should_delete_success_when_delete_product_not_existed() {
        givenMockMvc()
                .delete("/products/{id}", 1)
                .then()
                .statusCode(404);
    }


    @Test
    void should_update_success_when_update_product_existed() {
        productRepository.save(
                ProductEntity.builder()
                        .name("可乐")
                        .description("快乐水")
                        .imageUrl("http://####")
                        .price(new BigDecimal("2.5"))
                        .build()
        );
        Product productRequest = Product.builder()
                .name("元气森林")
                .description("无糖饮料，好喝！")
                .imageUrl("data:img/jpg;base64,iVBORw0KGgo")
                .price(new BigDecimal("4"))
                .build();

        givenMockMvc()
                .body(productRequest)
                .put("/products/{id}", 1)
                .then()
                .statusCode(200)
                .body("name", is("元气森林"));
    }

    @Test
    void should_update_success_when_update_product_not_existed() {
        Product productRequest = Product.builder()
                .name("元气森林")
                .description("无糖饮料，好喝！")
                .imageUrl("data:img/jpg;base64,iVBORw0KGgo")
                .price(new BigDecimal("4"))
                .build();

        givenMockMvc()
                .body(productRequest)
                .put("/products/{id}", 1)
                .then()
                .statusCode(404);
    }


    @Test
    void should_return_products_when_find_products_by_page_number_and_page_size() {
        productRepository.save(
                ProductEntity.builder()
                        .name("可乐")
                        .description("快乐水")
                        .imageUrl("http://####")
                        .price(new BigDecimal("2.5"))
                        .build()
        );
        productRepository.save(
                ProductEntity.builder()
                        .name("雪碧")
                        .description("大声你的逼逼")
                        .imageUrl("http://####")
                        .price(new BigDecimal("2.5"))
                        .build()
        );
        givenMockMvc()
                .param("pageNumber", 1)
                .param("pageSize", 10)
                .get("/products")
                .then()
                .statusCode(200)
                .body("currentPage", is(1))
                .body("totalPage", is(1))
                .body("data[0].name", is("雪碧"));
    }

}
