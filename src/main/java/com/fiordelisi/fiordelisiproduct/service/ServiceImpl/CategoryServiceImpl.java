package com.fiordelisi.fiordelisiproduct.service.ServiceImpl;

import com.fiordelisi.fiordelisiproduct.dto.CategoryDto;
import com.fiordelisi.fiordelisiproduct.dto.ProductVariantDto;
import com.fiordelisi.fiordelisiproduct.entity.Category;
import com.fiordelisi.fiordelisiproduct.entity.Product;
import com.fiordelisi.fiordelisiproduct.repository.CategoryRepository;
import com.fiordelisi.fiordelisiproduct.repository.ProductRepository;
import com.fiordelisi.fiordelisiproduct.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Page<Category> search(String query, Pageable pageable) {
        if (query == null || query.isBlank()) {
            return categoryRepository.findAll(pageable);
        }
        return categoryRepository.findByNameContainingIgnoreCase(query.trim(), pageable);
    }

    @Override
    public Optional<Category> findById(String id) {
        return categoryRepository.findById(id);
    }

    @Override
    public Category create(Category category) {
        category.setId(null);
        return categoryRepository.save(category);
    }

    @Override
    public Category update(String id, Category category) {
        category.setId(id);
        return categoryRepository.save(category);
    }

    @Override
    public void deleteById(String id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public Category saveFromDto(CategoryDto dto) {
        Category category = Category.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .build();
        if (dto.getId() == null || dto.getId().isBlank()) {
            return create(category);
        }
        return update(dto.getId(), category);
    }

    @Override
    public CategoryDto getCategoryDtoForForm(String id) {
        if (id == null) return new CategoryDto();
        Category category = findById(id).orElse(new Category());
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .build();
    }

    @Override
    public Map<String, String> getIdToNameMap() {
        return findAll().stream().collect(Collectors.toMap(Category::getId, Category::getName));
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
        }

    public Page<ProductVariantDto> getAllProductVariant(String categoryId, String keyword, String sort, int page, int size) {
        List<Product> allProducts;
        if (categoryId != null && !categoryId.isEmpty() && keyword != null && !keyword.isEmpty()) {
            allProducts = productRepository.findAll().stream()
                    .filter(product -> product.getCategoryId() != null && 
                                     product.getCategoryId().equals(categoryId) &&
                                     product.getName() != null &&
                                     product.getName().toLowerCase().contains(keyword.toLowerCase()))
                    .collect(Collectors.toList());
        } else if (categoryId != null && !categoryId.isEmpty()) {
            allProducts = productRepository.findAllByCategoryId(categoryId);
        } else if (keyword != null && !keyword.isEmpty()) {
            allProducts = productRepository.findAll().stream()
                    .filter(product -> product.getName() != null &&
                                     product.getName().toLowerCase().contains(keyword.toLowerCase()))
                    .collect(Collectors.toList());
        } else {
            allProducts = productRepository.findAll();
        }

        List<ProductVariantDto> allVariants = allProducts.stream()
                .flatMap(product -> product.getVariants().stream()
                        .map(variant -> ProductVariantDto.builder()
                                .productId(product.getId())
                                .productName(product.getName())
                                .productImage(product.getImage())
                                .variant(variant)
                                .build()))
                .collect(Collectors.toList());

        if (sort != null && !sort.isEmpty()) {
            switch (sort) {
                case "price_asc":
                    allVariants.sort((a, b) -> Double.compare(a.getVariant().getPrice(), b.getVariant().getPrice()));
                    break;
                case "price_desc":
                    allVariants.sort((a, b) -> Double.compare(b.getVariant().getPrice(), a.getVariant().getPrice()));
                    break;
                case "name_asc":
                    allVariants.sort((a, b) -> a.getProductName().compareToIgnoreCase(b.getProductName()));
                    break;
                case "name_desc":
                    allVariants.sort((a, b) -> b.getProductName().compareToIgnoreCase(a.getProductName()));
                    break;
                default:
                    break;
            }
        }

        int start = page * size;
        int end = Math.min(start + size, allVariants.size());
        
        List<ProductVariantDto> pageContent;
        if (start >= allVariants.size()) {
            pageContent = new ArrayList<>();
        } else {
            pageContent = allVariants.subList(start, end);
        }

        return new PageImpl<>(pageContent, PageRequest.of(page, size), allVariants.size());
    }
}


