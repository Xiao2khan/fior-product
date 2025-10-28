package com.fiordelisi.fiordelisiproduct.service.ServiceImpl;

import com.fiordelisi.fiordelisiproduct.constant.Language;
import com.fiordelisi.fiordelisiproduct.dto.CategoryDto;
import com.fiordelisi.fiordelisiproduct.dto.ProductDto;
import com.fiordelisi.fiordelisiproduct.entity.Category;
import com.fiordelisi.fiordelisiproduct.entity.LocalizedText;
import com.fiordelisi.fiordelisiproduct.entity.Product;
import com.fiordelisi.fiordelisiproduct.entity.response.TranslationEntry;
import com.fiordelisi.fiordelisiproduct.repository.CategoryRepository;
import com.fiordelisi.fiordelisiproduct.repository.ProductRepository;
import com.fiordelisi.fiordelisiproduct.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.*;

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
        String lang = dto.getLanguage() != null ? dto.getLanguage() : Language.VI.getCode();

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

        Category category = Category.builder()
                .id(dto.getId())
                .name(names)
                .description(descriptions)
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
        String lang = Language.EN.getCode();

        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getNameByLanguage(lang))
                .description(category.getDescriptionByLanguage(lang))
                .language(lang)
                .build();
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
        }



    private Pageable createPageable(String sort, int page, int size) {
        Sort sorting = Sort.unsorted();

        if (sort != null && !sort.isBlank()) {
            switch (sort) {
                case "price_asc":
                    sorting = Sort.by(Sort.Order.asc("variants.price"));
                    break;
                case "price_desc":
                    sorting = Sort.by(Sort.Order.desc("variants.price"));
                    break;
                case "name_asc":
                    sorting = Sort.by(Sort.Order.asc("name"));
                    break;
                case "name_desc":
                    sorting = Sort.by(Sort.Order.desc("name"));
                    break;
                default:
                    break;
            }
        }

        return PageRequest.of(Math.max(page, 0), Math.max(size, 1), sorting);
    }
}