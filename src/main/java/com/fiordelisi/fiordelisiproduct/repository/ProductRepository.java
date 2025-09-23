package com.fiordelisi.fiordelisiproduct.repository;

import com.fiordelisi.fiordelisiproduct.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProductRepository extends MongoRepository<Product, String> {
    List<Product> findAllByCategoryId(String categoryId);

    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);
}


