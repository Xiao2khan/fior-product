package com.fiordelisi.fiordelisiproduct.service.ServiceImpl;

import com.fiordelisi.fiordelisiproduct.dto.VariantDto;
import com.fiordelisi.fiordelisiproduct.entity.Product;
import com.fiordelisi.fiordelisiproduct.entity.Variant;
import com.fiordelisi.fiordelisiproduct.service.ProductService;
import com.fiordelisi.fiordelisiproduct.service.VariantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VariantServiceImpl implements VariantService {
    private final ProductService productService;

    @Override
    public List<Variant> listVariants(String productId) {
        return productService.listVariants(productId);
    }

    @Override
    public Product upsertVariant(String productId, Variant variant) {
        return productService.upsertVariant(productId, variant);
    }

    @Override
    public Product deleteVariant(String productId, String variantId) {
        return productService.deleteVariant(productId, variantId);
    }

    @Override
    public List<Variant> getAllVariants() {
        List<Product> products = productService.findAll();
        List<Variant> allVariants = new ArrayList<>();
        for (Product product : products) {
            if (product.getVariants() != null) {
                for (Variant variant : product.getVariants()) {
                    variant.setProductId(product.getId());
                    allVariants.add(variant);
                }
            }
        }
        return allVariants;
    }

    @Override
    public Page<Variant> searchVariantsByProduct(String productId, String query, Pageable pageable) {
        List<Variant> variants = listVariants(productId);
        String q = query == null ? "" : query.trim().toLowerCase();
        variants.sort((a, b) -> {
            String sa = String.valueOf(a.getSizeGram());
            String sb = String.valueOf(b.getSizeGram());
            return sa.compareToIgnoreCase(sb);
        });
        List<Variant> filtered = new ArrayList<>();
        for (Variant v : variants) {
            if (q.isEmpty() || String.valueOf(v.getSizeGram()).toLowerCase().contains(q)) {
                filtered.add(v);
            }
        }
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), filtered.size());
        List<Variant> content = start > end ? new ArrayList<>() : filtered.subList(start, end);
        return new PageImpl<>(content, pageable, filtered.size());
    }

    @Override
    public Page<Variant> searchAllVariants(String query, Pageable pageable) {
        List<Variant> all = getAllVariants();
        String q = query == null ? "" : query.trim().toLowerCase();
        all.sort((a, b) -> {
            String sa = String.valueOf(a.getSizeGram());
            String sb = String.valueOf(b.getSizeGram());
            return sa.compareToIgnoreCase(sb);
        });
        List<Variant> filtered = new ArrayList<>();
        for (Variant v : all) {
            if (q.isEmpty() || String.valueOf(v.getSizeGram()).toLowerCase().contains(q)) {
                filtered.add(v);
            }
        }
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), filtered.size());
        List<Variant> content = start > end ? new ArrayList<>() : filtered.subList(start, end);
        return new PageImpl<>(content, pageable, filtered.size());
    }

    @Override
    public List<Product> getAllProducts() {
        return productService.findAll();
    }

    @Override
    public VariantDto getVariantDtoForForm(String productId, String variantId) {
        if (variantId == null) return new VariantDto();
        for (Variant v : listVariants(productId)) {
            if (v.getId().equals(variantId)) {
                return VariantDto.builder()
                        .id(v.getId())
                        .sizeGram(v.getSizeGram())
                        .price(v.getPrice())
                        .quantity(v.getQuantity())
                        .build();
            }
        }
        return new VariantDto();
    }

    @Override
    public Product saveVariantFromDto(String productId, VariantDto dto, String variantId) {
        Variant variant = Variant.builder()
                .id(variantId)
                .productId(productId)
                .sizeGram(dto.getSizeGram())
                .price(dto.getPrice())
                .quantity(dto.getQuantity())
                .build();
        return upsertVariant(productId, variant);
    }
}
