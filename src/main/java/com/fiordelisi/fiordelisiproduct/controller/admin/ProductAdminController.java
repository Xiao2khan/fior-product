package com.fiordelisi.fiordelisiproduct.controller.admin;

import com.fiordelisi.fiordelisiproduct.controller.BaseController;
import com.fiordelisi.fiordelisiproduct.dto.ProductDto;
import com.fiordelisi.fiordelisiproduct.entity.Category;
import com.fiordelisi.fiordelisiproduct.service.CategoryService;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/products")
@RequiredArgsConstructor
public class ProductAdminController extends BaseController {

    private final ProductService productService;
    private final CategoryService categoryService;

    @GetMapping
    public String list(@RequestParam(value = "q", required = false) String q,
                       @RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "size", defaultValue = "10") int size,
                       Model model) {
        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.max(size, 1), Sort.by(Sort.Order.asc("name")));
        Page<com.fiordelisi.fiordelisiproduct.entity.Product> result = productService.search(q, pageable);
        model.addAttribute("page", result);
        model.addAttribute("products", result.getContent());
        model.addAttribute("q", q);
        model.addAttribute("categoryMap", categoryService.getIdToNameMap());
        return "admin/product/list";
    }

    @GetMapping({"/create", "/edit/{id}"})
    public String form(@PathVariable(required = false) String id, Model model) {
        ProductDto dto = productService.getProductDtoForForm(id);
        List<Category> categories = categoryService.findAll();
        model.addAttribute("product", dto);
        model.addAttribute("categories", categories);
        return "admin/product/form";
    }

    @PostMapping({"/create", "/edit/{id}"})
    public String submit(@PathVariable(required = false) String id,
                         @Valid @ModelAttribute("product") ProductDto dto,
                         BindingResult bindingResult,
                         RedirectAttributes ra,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.findAll());
            return "admin/product/form";
        }
        dto.setId(id);
        productService.saveFromDto(dto);
        ra.addFlashAttribute("success", id == null ? "Product created successfully" : "Product updated successfully");
        return "redirect:/admin/products";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable String id, RedirectAttributes ra) {
        productService.deleteById(id);
        ra.addFlashAttribute("success", "Product deleted successfully");
        return "redirect:/admin/products";
    }
}


