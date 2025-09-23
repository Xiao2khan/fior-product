package com.fiordelisi.fiordelisiproduct.service;

import com.fiordelisi.fiordelisiproduct.dto.VariantDto;
import com.fiordelisi.fiordelisiproduct.entity.Product;
import com.fiordelisi.fiordelisiproduct.entity.Variant;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface VariantService {
    List<Variant> listVariants(String productId);

    Page<Variant> searchVariantsByProduct(String productId, String query, Pageable pageable);

    Product upsertVariant(String productId, Variant variant);

    Product deleteVariant(String productId, String variantId);

    List<Variant> getAllVariants();

    Page<Variant> searchAllVariants(String query, Pageable pageable);

    List<Product> getAllProducts();

    VariantDto getVariantDtoForForm(String productId, String variantId);

    Product saveVariantFromDto(String productId, VariantDto dto, String variantId);
}
