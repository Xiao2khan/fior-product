package com.fiordelisi.fiordelisiproduct.service.ServiceImpl;

import com.fiordelisi.fiordelisiproduct.dto.CategoryDto;
import com.fiordelisi.fiordelisiproduct.entity.Category;
import com.fiordelisi.fiordelisiproduct.repository.CategoryRepository;
import com.fiordelisi.fiordelisiproduct.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

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
}


