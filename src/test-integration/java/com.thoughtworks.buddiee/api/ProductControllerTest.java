package com.thoughtworks.buddiee.api;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.thoughtworks.buddiee.base.ApiBaseTest;
import com.thoughtworks.buddiee.dto.Product;
import com.thoughtworks.buddiee.entity.ProductEntity;
import com.thoughtworks.buddiee.repository.ProductRepository;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static com.toomuchcoding.jsonassert.JsonAssertion.assertThatJson;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;


public class ProductControllerTest extends ApiBaseTest {

    @Autowired
    ProductRepository productRepository;


    @BeforeEach
    void setUp() {
        prepareProductData();
    }

    private void prepareProductData() {
        productRepository.save(
                ProductEntity.builder()
                .id(1L)
                .name("可乐")
                .description("快乐水")
                .imageUrl("http://####")
                .price(new BigDecimal("2.5"))
                .build()
        );
        productRepository.save(
                ProductEntity.builder()
                        .id(2L)
                        .name("雪碧")
                        .description("大声跟你逼逼")
                        .imageUrl("http://####")
                        .price(new BigDecimal("2.5"))
                        .build()
        );
        productRepository.save(
                ProductEntity.builder()
                        .id(3L)
                        .name("脉动")
                        .description("快乐水")
                        .imageUrl("http://####")
                        .price(new BigDecimal("4"))
                        .build()
        );
    }

    @Nested
    class FindProductById {

        @Test
        void should_return_product_info_when_product_existed() {
            RequestSpecification request = given().header("Content-Type", "application/json");

            Response response = given().spec(request)
                    .pathParam("id", 1)
                    .get("products/{id}");

            assertThat(response.statusCode()).isEqualTo(200);
            DocumentContext parsedJson = JsonPath.parse(response.getBody().asString());
            assertThatJson(parsedJson).field("['name']").isEqualTo("可乐");
        }

        @Test
        void should_throw_exception_when_product_not_existed() {
            RequestSpecification request = given().header("Content-Type", "application/json");

            Response response = given().spec(request)
                    .pathParam("id", 999)
                    .get("products/{id}");

            assertThat(response.statusCode()).isEqualTo(404);
        }
    }


    @Test
    void should_return_product_info_when_create_product_success() {
        Product productRequest = Product.builder()
                .name("元气森林")
                .description("无糖饮料，好喝！")
                .imageUrl("data:img/jpg;base64,iVBORw0KGgo")
                .price(new BigDecimal("4"))
                .build();

        RequestSpecification request = given().header("Content-Type", "application/json");

        Response response = given().spec(request)
                .body(productRequest)
                .post("products");

        assertThat(response.statusCode()).isEqualTo(201);
        DocumentContext parsedJson = JsonPath.parse(response.getBody().asString());
        assertThatJson(parsedJson).field("['name']").isEqualTo("元气森林");

    }

    @Nested
    class DeleteProductById {

        @Test
        void should_delete_success_when_product_existed() {
            RequestSpecification request = given().header("Content-Type", "application/json");

            Response response = given().spec(request)
                    .pathParam("id", 4)
                    .delete("products/{id}");

            assertThat(response.statusCode()).isEqualTo(204);
        }

        @Test
        void should_delete_success_when_product_not_existed() {
            RequestSpecification request = given().header("Content-Type", "application/json");

            Response response = given().spec(request)
                    .pathParam("id", 4)
                    .delete("products/{id}");

            assertThat(response.statusCode()).isEqualTo(404);
        }
    }


}
