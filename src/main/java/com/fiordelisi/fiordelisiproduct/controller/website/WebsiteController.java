package com.fiordelisi.fiordelisiproduct.controller.website;

import com.fiordelisi.fiordelisiproduct.dto.ProductVariantDto;
import com.fiordelisi.fiordelisiproduct.entity.Category;
import com.fiordelisi.fiordelisiproduct.entity.Product;
import com.fiordelisi.fiordelisiproduct.entity.Variant;
import com.fiordelisi.fiordelisiproduct.service.CategoryService;
import com.fiordelisi.fiordelisiproduct.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping
@RequiredArgsConstructor
public class WebsiteController {

    private final ProductService productService;
    private final CategoryService categoryService;

    @GetMapping("/{lang}")
    public String index(Model model, @PathVariable String lang) {
        List<Product> products = productService.findAll();
        log.info("products: {}", products);
        List<ProductVariantDto> variants = products.stream()
                .flatMap(product -> product.getVariants().stream()
                        .map(variant -> new ProductVariantDto(product.getId(), product.getName(), product.getImage(), variant)))
                .collect(Collectors.toList());
        model.addAttribute("variants", variants);
        model.addAttribute("lang", lang);
        return "website/home-page";
    }

    @GetMapping("/{lang}/products")
    public String products(
            @PathVariable String lang,
            @RequestParam(required = false) String categoryId,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            Model model) {

        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("lang", lang);

        Page<ProductVariantDto> productVariantPage = categoryService.getAllProductVariant(categoryId, search, sort, page, size);
        model.addAttribute("variants", productVariantPage.getContent());
        model.addAttribute("totalPages", productVariantPage.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("selectedCategoryId", categoryId);
        model.addAttribute("search", search != null ? search : "");
        model.addAttribute("selectedSort", sort);
        return "website/list-variants";
    }
}
