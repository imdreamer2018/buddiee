package com.thoughtworks.buddiee.controller;

import com.thoughtworks.buddiee.dto.Product;
import com.thoughtworks.buddiee.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product createProduct(@RequestBody Product product) {
        return productService.createProduct(product);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable(name = "id") Long productId) {
        productService.deleteProduct(productId);
    }

    @GetMapping("/{id}")
    public Product findProduct(@PathVariable(name = "id") Long productId) {
        return productService.findProduct(productId);
    }

    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable(name = "id") Long productId,
                                 @RequestBody Product updateProduct) {
        return productService.updateProduct(productId, updateProduct);
    }
}
