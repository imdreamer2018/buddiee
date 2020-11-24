package com.thoughtworks.buddiee.repository;

import com.thoughtworks.buddiee.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    Page<ProductEntity> findAll(Pageable pageable);
}
