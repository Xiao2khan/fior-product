package com.fiordelisi.fiordelisiproduct.repository;

import com.fiordelisi.fiordelisiproduct.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProductRepository extends MongoRepository<Product, String> {
    List<Product> findAllByCategoryId(String categoryId);

    Page<Product> findByCategoryId(String categoryId, Pageable pageable);

    Page<Product> findByCategoryIdAndNameContainingIgnoreCase(String categoryId, String keyword, Pageable pageable);

    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);
}


