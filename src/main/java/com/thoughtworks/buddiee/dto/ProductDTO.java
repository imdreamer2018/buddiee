package com.thoughtworks.buddiee.dto;

import com.thoughtworks.buddiee.entity.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProductDTO {

    private Long id;
    @NotNull(message = "product name can not empty")
    private String name;
    @NotNull(message = "product description can not empty")
    private String description;
    @NotNull(message = "product image url can not empty")
    private String imageUrl;
    @NotNull(message = "product price can not empty")
    @Min(value = 0, message = "product price must be greater than 0")
    private BigDecimal price;

    public ProductEntity toProductEntity() {
        return ProductEntity.builder()
                .id(this.getId())
                .name(this.getName())
                .description(this.getDescription())
                .imageUrl(this.getImageUrl())
                .price(this.getPrice())
                .build();
    }
}
