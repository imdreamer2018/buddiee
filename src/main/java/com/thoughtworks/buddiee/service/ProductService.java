package com.thoughtworks.buddiee.service;

import com.thoughtworks.buddiee.dto.Page;
import com.thoughtworks.buddiee.dto.ProductDTO;
import com.thoughtworks.buddiee.entity.ProductEntity;
import com.thoughtworks.buddiee.exception.ResourceNotFoundException;
import com.thoughtworks.buddiee.mapper.ProductMapper;
import com.thoughtworks.buddiee.repository.ProductRepository;
import com.thoughtworks.buddiee.util.AliyunOssUtil;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProductService {

    public static final String CAN_NOT_FIND_BASIC_INFO_OF_PRODUCT_WITH_ID_IS = "can not find basic info of product with id is ";
    private final ProductRepository productRepository;
    private final AliyunOssUtil aliyunOssUtil;
    private final ProductMapper productMapper = Mappers.getMapper(ProductMapper.class);

    public ProductDTO createProduct(ProductDTO productDTO) throws IOException {
        String url = aliyunOssUtil.uploadBase64FileToAliyunOss("image/product/", productDTO.getImageUrl());
        productDTO.setImageUrl(url);
        ProductEntity productEntity = productMapper.productDtoToProductEntity(productDTO);
        productRepository.save(productEntity);
        return productMapper.productEntityToProductDto(productEntity);
    }

    public void deleteProduct(Long productId) {
        ProductEntity productEntity = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(CAN_NOT_FIND_BASIC_INFO_OF_PRODUCT_WITH_ID_IS + productId));
        String productImageAbsolutePath = productEntity.getImageUrl().split("com/")[1];
        aliyunOssUtil.deleteFile(productImageAbsolutePath);
        productRepository.deleteById(productId);
    }

    public ProductDTO findProduct(Long productId) {
        ProductEntity productEntity = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(CAN_NOT_FIND_BASIC_INFO_OF_PRODUCT_WITH_ID_IS + productId));
        return productMapper.productEntityToProductDto(productEntity);
    }

    public ProductDTO updateProduct(Long productId, ProductDTO updateProductDTO) {
        ProductEntity productEntity = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(CAN_NOT_FIND_BASIC_INFO_OF_PRODUCT_WITH_ID_IS + productId));
        updateProductDTO.setId(productEntity.getId());
        productRepository.save(productMapper.productDtoToProductEntity(updateProductDTO));
        return updateProductDTO;
    }

    public Page<ProductDTO> findProducts(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by("id").descending());
        org.springframework.data.domain.Page<ProductEntity> productEntities = productRepository.findAll(pageable);
        List<ProductDTO> productDTOS = productEntities.stream().map(productMapper::productEntityToProductDto).collect(Collectors.toList());
        Page<ProductDTO> page = new Page<>();
        page.setCurrentPage(pageNumber);
        page.setTotalPage(productEntities.getTotalPages());
        page.setData(productDTOS);
        return page;
    }
}
