package com.fiordelisi.fiordelisiproduct.controller.website;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fiordelisi.fiordelisiproduct.dto.CategoryDto;
import com.fiordelisi.fiordelisiproduct.dto.ProductDto;
import com.fiordelisi.fiordelisiproduct.dto.QuestionDto;
import com.fiordelisi.fiordelisiproduct.entity.Category;
import com.fiordelisi.fiordelisiproduct.entity.Product;
import com.fiordelisi.fiordelisiproduct.entity.Question;
import com.fiordelisi.fiordelisiproduct.service.CategoryService;
import com.fiordelisi.fiordelisiproduct.service.ProductService;
import com.fiordelisi.fiordelisiproduct.service.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping
@RequiredArgsConstructor
public class WebsiteController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final QuestionService questionService;

    @GetMapping("/{lang}")
    public String index(
            @PathVariable String lang,
            Model model) {
        List<Product> productList = productService.findAll();
        List<ProductDto> products = productList.stream()
                .map(product -> ProductDto.toDto(product, lang.toUpperCase()))
                .toList();

        model.addAttribute("products", products);
        model.addAttribute("lang", lang);
        try {
            ObjectMapper mapper = new ObjectMapper();
            // optional: pretty / stable output
            mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            String productsJson = mapper.writeValueAsString(products);
            model.addAttribute("productsJson", productsJson);
        } catch (Exception e) {
            model.addAttribute("productsJson", "[]");
        }

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
        List<Category>  categories = categoryService.getAllCategories();
        List<CategoryDto> categoryDtos = categories.stream()
                .map(cat -> CategoryDto.toDto(cat, lang.toUpperCase()))
                .toList();
        model.addAttribute("categories", categoryDtos);
        String languageCode = lang.toLowerCase();
        model.addAttribute("lang", lang);

        size = Math.max(1, Math.min(size, 100));
        page = Math.max(0, page);

        Sort sortObj = Sort.unsorted();
        if (sort != null && !sort.isBlank()) {
            switch (sort) {
                case "price_asc":
                    sortObj = Sort.by("variants.price").ascending().and(Sort.by("defaultPrice").ascending());
                    break;
                case "price_desc":
                    sortObj = Sort.by("variants.price").descending().and(Sort.by("defaultPrice").descending());
                    break;
                case "name_asc":
                    sortObj = Sort.by("name").ascending();
                    break;
                case "name_desc":
                    sortObj = Sort.by("name").descending();
                    break;
            }
        }

        Pageable pageable = PageRequest.of(page, size, sortObj);

        Page<Product> productPage;
        if (categoryId != null && !categoryId.isBlank() && search != null && !search.isBlank()) {
            productPage = productService.findByCategoryIdAndNameContainingIgnoreCase(categoryId, search,languageCode, pageable);
        } else if (categoryId != null && !categoryId.isBlank()) {
            productPage = productService.findByCategoryId(categoryId, pageable);
        } else if (search != null && !search.isBlank()) {
            productPage = productService.search(search,languageCode, pageable);
        } else {
            productPage = productService.findAll(pageable);
        }

        List<ProductDto> products = productPage.getContent().stream()
                .map(product -> ProductDto.toDto(product, lang.toUpperCase()))
                .toList();

        if (page >= productPage.getTotalPages() && productPage.getTotalPages() > 0) {
            String redirectUrl = String.format("/%s/products?page=%d&size=%d", lang, productPage.getTotalPages() - 1, size);
            if (categoryId != null && !categoryId.isBlank()) {
                redirectUrl += "&categoryId=" + categoryId;
            }
            if (search != null && !search.isBlank()) {
                redirectUrl += "&search=" + search;
            }
            if (sort != null && !sort.isBlank()) {
                redirectUrl += "&sort=" + sort;
            }
            return "redirect:" + redirectUrl;
        }

        model.addAttribute("products", products);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("selectedCategoryId", categoryId);
        model.addAttribute("search", search != null ? search : "");
        model.addAttribute("selectedSort", sort);

        return "website/list-variants";
    }

    @GetMapping("/{lang}/support")
    public  String support(@PathVariable String lang,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "4") int size,
            Model model){
        String languageCode = lang.toLowerCase();
        Pageable pageable = PageRequest.of(page, size);
        Page<Question> questionPage;
        questionPage = questionService.search(search,languageCode, pageable);
        log.info(questionPage.toString());

        List<QuestionDto> questions = questionPage.getContent().stream()
                .map(question -> QuestionDto.toDto(question, lang.toUpperCase()))
                .toList();

        model.addAttribute("questions", questions);
        return "website/support";
    }
}
