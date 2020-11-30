package com.thoughtworks.buddiee.controller;

import com.thoughtworks.buddiee.dto.Page;
import com.thoughtworks.buddiee.dto.ProductDTO;
import com.thoughtworks.buddiee.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.IOException;

@RestController
@Validated
@RequestMapping(value = "/api/products")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProductController {

    public static final String PRODUCT_ID_MUSE_BE_NUMBER_AND_GREATER_THAN_1 = "product id muse be number and greater than 1";
    private final ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDTO createProduct(@RequestBody @Valid ProductDTO productDTO) throws IOException {
        return productService.createProduct(productDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(
            @NotNull(message = "product id can not be null")
            @Min(value = 1, message = PRODUCT_ID_MUSE_BE_NUMBER_AND_GREATER_THAN_1)
            @PathVariable(name = "id") Long productId) {
        productService.deleteProduct(productId);
    }

    @GetMapping("/{id}")
    public ProductDTO findProduct(
            @NotNull(message = "product id can not be null")
            @Min(value = 1, message = PRODUCT_ID_MUSE_BE_NUMBER_AND_GREATER_THAN_1)
            @PathVariable(name = "id") Long productId) {
        return productService.findProduct(productId);
    }

    @PutMapping("/{id}")
    public ProductDTO updateProduct(
            @NotNull(message = "product id can not be null")
            @Min(value = 1, message = PRODUCT_ID_MUSE_BE_NUMBER_AND_GREATER_THAN_1)
            @PathVariable(name = "id") Long productId,
            @RequestBody @Valid ProductDTO updateProductDTO) {
        return productService.updateProduct(productId, updateProductDTO);
    }

    @GetMapping
    public Page<ProductDTO> findProducts(
            @Min(value = 1, message = PRODUCT_ID_MUSE_BE_NUMBER_AND_GREATER_THAN_1)
            @RequestParam(required = false, defaultValue = "1") int pageNumber,
            @Min(value = 1, message = PRODUCT_ID_MUSE_BE_NUMBER_AND_GREATER_THAN_1)
            @RequestParam(required = false, defaultValue = "10") int pageSize) {
        return productService.findProducts(pageNumber, pageSize);
    }
}
