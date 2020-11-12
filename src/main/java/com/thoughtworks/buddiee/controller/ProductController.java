package com.thoughtworks.buddiee.controller;

import com.thoughtworks.buddiee.dto.Page;
import com.thoughtworks.buddiee.dto.Product;
import com.thoughtworks.buddiee.service.ProductService;
import org.springframework.http.HttpStatus;
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

    @GetMapping
    public Page<Product> findProducts(@RequestParam(required = false, defaultValue = "1") int pageNumber,
                                      @RequestParam(required = false, defaultValue = "10") int pageSize) {
        return productService.findProducts(pageNumber, pageSize);
    }
}
