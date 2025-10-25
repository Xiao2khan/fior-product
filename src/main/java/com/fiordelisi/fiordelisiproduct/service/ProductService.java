package com.fiordelisi.fiordelisiproduct.service;

import com.fiordelisi.fiordelisiproduct.dto.ProductDto;
import com.fiordelisi.fiordelisiproduct.entity.Product;
import com.fiordelisi.fiordelisiproduct.entity.Variant;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> findAll();

    Page<Product> search(String query, String languageCode, Pageable pageable);

    Optional<Product> findById(String id);

    Product create(Product product);

    Product update(String id, Product product);

    void deleteById(String id);

    // Variants management within a product
    List<Variant> listVariants(String productId);

    Product upsertVariant(String productId, Variant variant);

    Product deleteVariant(String productId, String variantId);

    // Higher-level API for controllers
    Product saveFromDto(ProductDto dto);

    ProductDto getProductDtoForForm(String id);

    Page<Product> findAll(Pageable pageable);
    Page<Product> findByCategoryId(String categoryId, Pageable pageable);
    Page<Product> findByCategoryIdAndNameContainingIgnoreCase(String categoryId, String keyword,String languageCode, Pageable pageable);
}


