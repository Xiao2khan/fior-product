package com.fiordelisi.fiordelisiproduct.service;

import com.fiordelisi.fiordelisiproduct.dto.CategoryDto;
import com.fiordelisi.fiordelisiproduct.dto.ProductDto;
import com.fiordelisi.fiordelisiproduct.entity.Category;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Map;

public interface CategoryService {
    List<Category> findAll();

    Page<Category> search(String query, Pageable pageable);

    Optional<Category> findById(String id);

    Category create(Category category);

    Category update(String id, Category category);

    void deleteById(String id);

    Category saveFromDto(CategoryDto dto);

    CategoryDto getCategoryDtoForForm(String id);

    List<Category> getAllCategories();

}


