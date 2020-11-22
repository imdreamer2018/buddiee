package com.thoughtworks.buddiee.repository;

import com.thoughtworks.buddiee.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
}
