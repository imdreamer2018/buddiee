package com.thoughtworks.buddiee.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Product {

    private Long id;
    @NotNull(message = "product name can not empty")
    private String name;
    @NotNull(message = "product description can not empty")
    private String description;
    @NotNull(message = "product image url can not empty")
    private String imageUrl;
    @NotNull(message = "product price can not empty")
    @Min(value = 0, message = "product price must be greater than 0")
    private Integer price;
}
