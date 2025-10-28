package com.fiordelisi.fiordelisiproduct.service.ServiceImpl;

import com.fiordelisi.fiordelisiproduct.constant.Language;
import com.fiordelisi.fiordelisiproduct.dto.ProductDto;
import com.fiordelisi.fiordelisiproduct.entity.LocalizedText;
import com.fiordelisi.fiordelisiproduct.entity.Product;
import com.fiordelisi.fiordelisiproduct.entity.Variant;
import com.fiordelisi.fiordelisiproduct.entity.response.TranslationEntry;
import com.fiordelisi.fiordelisiproduct.repository.ProductRepository;
import com.fiordelisi.fiordelisiproduct.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Page<Product> search(String query, String languageCode, Pageable pageable) {
        if (query == null || query.isBlank()) {
            return productRepository.findAll(pageable);
        }
        return productRepository.findByNameContainingIgnoreCaseAndLanguageWithFallback(query.trim(), languageCode, pageable);
    }

    @Override
    public Optional<Product> findById(String id) {
        return productRepository.findById(id);
    }

    @Override
    public Product create(Product product) {
        product.setId(null);
        // ensure variant ids
        if (product.getVariants() != null) {
            for (Variant v : product.getVariants()) {
                if (v.getId() == null || v.getId().isBlank()) {
                    v.setId(UUID.randomUUID().toString());
                }
                v.setProductId(product.getId());
            }
        }
        return productRepository.save(product);
    }

    @Override
    public Product update(String id, Product product) {
        product.setId(id);
        return productRepository.save(product);
    }

    @Override
    public void deleteById(String id) {
        productRepository.deleteById(id);
    }

    @Override
    public List<Variant> listVariants(String productId) {
        return findById(productId).map(Product::getVariants).orElseGet(ArrayList::new);
    }

    @Override
    public Product upsertVariant(String productId, Variant variant) {
        Product product = productRepository.findById(productId).orElseThrow();
        List<Variant> variants = product.getVariants();
        if (variants == null) variants = new ArrayList<>();

        if (variant.getId() == null || variant.getId().isBlank()) {
            variant.setId(UUID.randomUUID().toString());
            variant.setProductId(productId);
            variants.add(variant);
        } else {
            for (int i = 0; i < variants.size(); i++) {
                if (variants.get(i).getId().equals(variant.getId())) {
                    variant.setProductId(productId);
                    variants.set(i, variant);
                    break;
                }
            }
        }
        product.setVariants(variants);
        return productRepository.save(product);
    }

    @Override
    public Product deleteVariant(String productId, String variantId) {
        Product product = productRepository.findById(productId).orElseThrow();
        List<Variant> variants = product.getVariants();
        if (variants != null) {
            variants.removeIf(v -> v.getId().equals(variantId));
        }
        product.setVariants(variants);
        return productRepository.save(product);
    }

    @Override
    public Product saveFromDto(ProductDto dto) {
        Product product;
        String lang = dto.getLanguage() != null ? dto.getLanguage() : Language.VI.getCode();
        if (dto.getId() == null || dto.getId().isBlank()) {
            product = new Product();
            product.setId(UUID.randomUUID().toString());
        } else {
            product = findById(dto.getId()).orElse(new Product());
        }

        Set<LocalizedText> names = new HashSet<>();
        Set<LocalizedText> descriptions = new HashSet<>();

        names.add(new LocalizedText(lang, dto.getName()));
        descriptions.add(new LocalizedText(lang, dto.getDescription()));

        if (dto.getTranslations() != null) {
            for (TranslationEntry t : dto.getTranslations()) {
                if (t.getLanguage() != null && t.getName() != null && !t.getName().isBlank()) {
                    names.add(new LocalizedText(t.getLanguage(), t.getName()));
                    descriptions.add(new LocalizedText(t.getLanguage(), t.getDescription()));
                }
            }
        }
        product.setName(names);
        product.setDescription(descriptions);
        product.setCategoryId(dto.getCategoryId() != null ? dto.getCategoryId() : new ArrayList<>());
        product.setDefaultPrice(dto.getDefaultPrice() != null ? dto.getDefaultPrice() : 0);
        product.setImages(dto.getImages() != null ? dto.getImages() : new ArrayList<>());
        product.setInStock(dto.isInStock());
        // Xử lý variants
        List<Variant> currentVariants = product.getVariants() != null ? new ArrayList<>(product.getVariants()) : new ArrayList<>();

        // Xóa các variant có trong removedVariantIds
        if (dto.getRemovedVariantIds() != null && !dto.getRemovedVariantIds().isEmpty()) {
            currentVariants.removeIf(variant -> dto.getRemovedVariantIds().contains(variant.getId()));
        }

        List<Variant> updatedVariants = new ArrayList<>();
        if (dto.getVariants() != null) {
            for (Variant incoming : dto.getVariants()) {
                if (incoming == null) continue;

                Variant variant;
                if (incoming.getId() != null && !incoming.getId().isBlank()) {
                    // Tìm variant hiện có
                    variant = currentVariants.stream()
                            .filter(v -> v.getId().equals(incoming.getId()))
                            .findFirst()
                            .orElse(new Variant());
                    variant.setId(incoming.getId());
                } else {
                    // Tạo variant mới
                    variant = new Variant();
                    variant.setId(UUID.randomUUID().toString());
                }

                variant.setProductId(product.getId());
                variant.setSize(incoming.getSize());
                variant.setPrice(incoming.getPrice());
                variant.setInstock(incoming.isInstock());
                variant.setUnit(incoming.getUnit());
                updatedVariants.add(variant);
            }
        }

        product.setVariants(updatedVariants);

        // Lưu product
        if (dto.getId() == null || dto.getId().isBlank()) {

            return create(product);
        } else {
            return update(dto.getId(), product);
        }
    }


    @Override
    public ProductDto getProductDtoForForm(String id) {
        if (id == null) return new ProductDto();

        try {
            Product product = findById(id).orElse(new Product());
            String defaultLang = Language.VI.getCode();

            String nameDefault = product.getNameByLanguage(defaultLang);
            String descDefault = product.getDescriptionByLanguage(defaultLang);

            List<TranslationEntry> translations = new ArrayList<>();
            for (LocalizedText nameEntry : product.getName()) {
                if (nameEntry.getLanguage().equalsIgnoreCase(defaultLang)) continue;

                TranslationEntry entry = new TranslationEntry();
                entry.setLanguage(nameEntry.getLanguage());
                entry.setName(nameEntry.getValue());

                product.getDescription().stream()
                        .filter(desc -> desc.getLanguage().equalsIgnoreCase(nameEntry.getLanguage()))
                        .findFirst()
                        .ifPresent(desc -> entry.setDescription(desc.getValue()));

                translations.add(entry);
            }

            return ProductDto.builder()
                    .id(product.getId())
                    .name(nameDefault)
                    .description(descDefault)
                    .images(product.getImages() != null ? new ArrayList<>(product.getImages()) : new ArrayList<>())
                    .categoryId(product.getCategoryId() != null ? new ArrayList<>(product.getCategoryId()) : new ArrayList<>())
                    .defaultPrice(product.getDefaultPrice() != null ? product.getDefaultPrice() : 0)
                    .variants(product.getVariants() != null ? new ArrayList<>(product.getVariants()) : new ArrayList<>())
                    .language(defaultLang)
                    .inStock(product.isInStock())
                    .translations(translations)
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ProductDto();
    }
    @Override
    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Page<Product> findAll_pageable(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public Page<Product> findByCategoryId(String categoryId, Pageable pageable) {
        return productRepository.findByCategoryId(categoryId, pageable);
    }

    @Override
    public Page<Product> findByCategoryIdAndNameContainingIgnoreCase(String categoryId, String keyword,String languageCode, Pageable pageable) {
        return productRepository.findByCategoryIdAndNameContainingIgnoreCaseAndLanguageWithFallback(categoryId, keyword,languageCode, pageable);
    }

}


