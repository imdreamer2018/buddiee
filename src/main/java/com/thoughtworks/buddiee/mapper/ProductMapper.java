package com.thoughtworks.buddiee.mapper;

import com.thoughtworks.buddiee.dto.ProductDTO;
import com.thoughtworks.buddiee.entity.ProductEntity;
import org.mapstruct.Mapper;

@Mapper
public interface ProductMapper {

    ProductDTO productEntityToProductDto(ProductEntity productEntity);

    ProductEntity productDtoToProductEntity(ProductDTO productDTO);
}
