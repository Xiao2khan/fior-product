package com.fiordelisi.fiordelisiproduct.controller.admin;

import com.fiordelisi.fiordelisiproduct.controller.BaseController;
import com.fiordelisi.fiordelisiproduct.dto.CategoryDto;
import com.fiordelisi.fiordelisiproduct.service.CategoryService;
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

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class CategoryAdminController extends BaseController {

    private final CategoryService categoryService;

    @GetMapping
    public String list(@RequestParam(value = "q", required = false) String q,
                       @RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "size", defaultValue = "10") int size,
                       Model model) {
        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.max(size, 1), Sort.by(Sort.Order.asc("name")));
        Page<com.fiordelisi.fiordelisiproduct.entity.Category> result = categoryService.search(q, pageable);
        model.addAttribute("page", result);
        model.addAttribute("categories", result.getContent());
        model.addAttribute("q", q);
        return "admin/category/list";
    }

    @GetMapping({"/create", "/edit/{id}"})
    public String form(@PathVariable(required = false) String id, Model model) {
        CategoryDto dto = categoryService.getCategoryDtoForForm(id);
        model.addAttribute("category", dto);
        return "admin/category/form";
    }

    @PostMapping({"/create", "/edit/{id}"})
    public String submit(@PathVariable(required = false) String id,
                         @Valid @ModelAttribute("category") CategoryDto dto,
                         BindingResult bindingResult,
                         RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            return "admin/category/form";
        }
        dto.setId(id);
        categoryService.saveFromDto(dto);
        ra.addFlashAttribute("success", id == null ? "Category created successfully" : "Category updated successfully");
        return "redirect:/admin/categories";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable String id, RedirectAttributes ra) {
        categoryService.deleteById(id);
        ra.addFlashAttribute("success", "Category deleted successfully");
        return "redirect:/admin/categories";
    }
}


