package com.fiordelisi.fiordelisiproduct.controller.website;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fiordelisi.fiordelisiproduct.dto.CategoryDto;
import com.fiordelisi.fiordelisiproduct.dto.ProductDto;
import com.fiordelisi.fiordelisiproduct.entity.Category;
import com.fiordelisi.fiordelisiproduct.entity.Product;
import com.fiordelisi.fiordelisiproduct.service.CategoryService;
import com.fiordelisi.fiordelisiproduct.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

@Slf4j
@Controller
@RequestMapping
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    @GetMapping("/{lang}/product/{id}")
    public String index(
            @PathVariable String lang,
            @PathVariable String id,
            Model model) {

        ProductDto productDto = productService.findById(id)
                .map(product -> ProductDto.toDto(product, lang.toUpperCase()))
                .orElse(null);

        if (productDto == null) {
            return "redirect:/error/404";
        }

        List<String> categoryIds = productDto.getCategoryId();
        List<ProductDto> relatedProducts = new ArrayList<>();
        Set<String> addedIds = new HashSet<>();

        if (categoryIds != null && !categoryIds.isEmpty()) {

            int totalLimit = 20;
            int numCategories = categoryIds.size();


            int baseLimit = totalLimit / numCategories;
            int remainder = totalLimit % numCategories;

            for (int i = 0; i < numCategories; i++) {
                if (relatedProducts.size() >= totalLimit) break;

                String categoryId = categoryIds.get(i);


                int limitForThisCategory = baseLimit + (i < remainder ? 1 : 0);
                Pageable pageable = PageRequest.of(0, limitForThisCategory);

                Page<Product> productPage = productService.findByCategoryId(categoryId, pageable);

                productPage.getContent().stream()
                        .filter(p -> !p.getId().equals(productDto.getId()))
                        .filter(p -> addedIds.add(p.getId()))
                        .map(p -> ProductDto.toDto(p, lang.toUpperCase()))
                        .limit(limitForThisCategory)
                        .forEach(relatedProducts::add);

                if (relatedProducts.size() >= totalLimit) break;
            }
        }
        model.addAttribute("product", productDto);
        model.addAttribute("relatedProducts", relatedProducts);
        return "website/product-detail";
    }

}
