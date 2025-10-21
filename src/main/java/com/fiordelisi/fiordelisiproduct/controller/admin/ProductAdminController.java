package com.fiordelisi.fiordelisiproduct.controller.admin;

import com.fiordelisi.fiordelisiproduct.constant.Language;
import com.fiordelisi.fiordelisiproduct.controller.BaseController;
import com.fiordelisi.fiordelisiproduct.dto.CategoryDto;
import com.fiordelisi.fiordelisiproduct.dto.ProductDto;
import com.fiordelisi.fiordelisiproduct.entity.Category;
import com.fiordelisi.fiordelisiproduct.entity.Product;
import com.fiordelisi.fiordelisiproduct.service.CategoryService;
import com.fiordelisi.fiordelisiproduct.service.FileStorageService;
import com.fiordelisi.fiordelisiproduct.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Controller
@RequestMapping("/admin/products")
@RequiredArgsConstructor
public class ProductAdminController extends BaseController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final FileStorageService fileStorageService;

    @GetMapping
    public String list(@RequestParam(value = "q", required = false) String q,
                       @RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "size", defaultValue = "10") int size,
                       Model model) {
        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.max(size, 1), Sort.by(Sort.Order.asc("name")));
        Page<Product> result = productService.search(q, "vi", pageable);

        List<ProductDto> dtoList = result.getContent().stream()
                .map(product -> ProductDto.toDto(product, Language.VI.getCode()))
                .toList();

        model.addAttribute("page", result);
        model.addAttribute("products", dtoList);
        model.addAttribute("q", q);
        return "admin/product/list";
    }

    @GetMapping({"/create", "/edit/{id}"})
    public String form(@PathVariable(required = false) String id, Model model) {
        ProductDto dto = productService.getProductDtoForForm(id);
        List<Category> categories = categoryService.findAll();
        List<CategoryDto> categoryDtos = categories.stream()
                .map(cat -> CategoryDto.toDto(cat, "VI"))
                .toList();

        model.addAttribute("product", dto);
        model.addAttribute("categories", categoryDtos);
        return "admin/product/form";
    }

    @PostMapping({"/create", "/edit/{id}"})
    public String submit(@PathVariable(required = false) String id,
                         @Valid @ModelAttribute("product") ProductDto productDto,
                         BindingResult bindingResult,
                         @RequestParam(value = "imageFiles", required = false) MultipartFile[] uploadedFiles,
                         RedirectAttributes redirectAttributes,
                         Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.findAll());
            return "admin/product/form";
        }

        List<String> currentImages = productDto.getImages() != null
                ? new ArrayList<>(productDto.getImages())
                : new ArrayList<>();

        List<String> uploadedPaths = fileStorageService.storeFiles(uploadedFiles);
        currentImages.addAll(uploadedPaths);

        if (id != null && !id.isBlank()) {
            ProductDto existingProduct = productService.getProductDtoForForm(id);
            List<String> existingImages = existingProduct.getImages();

            if (existingImages != null) {
                for (String img : existingImages) {
                    if (!currentImages.contains(img)) {
                        currentImages.add(img);
                    }
                }
            }
        }

        if (productDto.getRemovedImages() != null && !productDto.getRemovedImages().isEmpty()) {
            fileStorageService.deleteFiles(productDto.getRemovedImages());
            currentImages.removeAll(productDto.getRemovedImages());
        }

        productDto.setImages(currentImages);
        productDto.setId(id);

        productService.saveFromDto(productDto);

        redirectAttributes.addFlashAttribute("success",
                id == null ? "Product created successfully" : "Product updated successfully");

        return "redirect:/admin/products";
    }


    @PostMapping("/delete/{id}")
    public String delete(@PathVariable String id, RedirectAttributes ra) {
        productService.deleteById(id);
        ra.addFlashAttribute("success", "Product deleted successfully");
        return "redirect:/admin/products";
    }
}


