package com.fiordelisi.fiordelisiproduct.service.ServiceImpl;

import com.fiordelisi.fiordelisiproduct.dto.ProductDto;
import com.fiordelisi.fiordelisiproduct.entity.Nutrition;
import com.fiordelisi.fiordelisiproduct.entity.Product;
import com.fiordelisi.fiordelisiproduct.entity.Variant;
import com.fiordelisi.fiordelisiproduct.repository.ProductRepository;
import com.fiordelisi.fiordelisiproduct.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Page<Product> search(String query, Pageable pageable) {
        if (query == null || query.isBlank()) {
            return productRepository.findAll(pageable);
        }
        return productRepository.findByNameContainingIgnoreCase(query.trim(), pageable);
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
        Nutrition nutrition = null;
        if (dto.getNutrition() != null) {
            nutrition = Nutrition.builder()
                    .carbohydrate(dto.getNutrition().getCarbohydrate())
                    .protein(dto.getNutrition().getProtein())
                    .fat(dto.getNutrition().getFat())
                    .fiber(dto.getNutrition().getFiber())
                    .salt(dto.getNutrition().getSalt())
                    .energy(dto.getNutrition().getEnergy())
                    .build();
        }
        Product product = Product.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .image(dto.getImage())
                .categoryId(dto.getCategoryId())
                .nutrition(nutrition)
                .build();
        if (dto.getId() == null || dto.getId().isBlank()) {
            return create(product);
        }
        return update(dto.getId(), product);
    }

    @Override
    public ProductDto getProductDtoForForm(String id) {
        if (id == null) return new ProductDto();
        Product product = findById(id).orElse(new Product());
        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setImage(product.getImage());
        dto.setCategoryId(product.getCategoryId());
        if (product.getNutrition() != null) {
            dto.setNutrition(com.fiordelisi.fiordelisiproduct.dto.NutritionDto.builder()
                    .carbohydrate(product.getNutrition().getCarbohydrate())
                    .protein(product.getNutrition().getProtein())
                    .fat(product.getNutrition().getFat())
                    .fiber(product.getNutrition().getFiber())
                    .salt(product.getNutrition().getSalt())
                    .energy(product.getNutrition().getEnergy())
                    .build());
        }
        return dto;
    }
}


