package com.fiordelisi.fiordelisiproduct.controller.admin;

import com.fiordelisi.fiordelisiproduct.constant.Language;
import com.fiordelisi.fiordelisiproduct.controller.BaseController;
import com.fiordelisi.fiordelisiproduct.dto.CategoryDto;
import com.fiordelisi.fiordelisiproduct.dto.LanguageDto;
import com.fiordelisi.fiordelisiproduct.entity.Category;
import com.fiordelisi.fiordelisiproduct.entity.LocalizedText;
import com.fiordelisi.fiordelisiproduct.entity.response.TranslationEntry;
import com.fiordelisi.fiordelisiproduct.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
@Slf4j
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

        // 🔹 Map entity -> DTO
        List<CategoryDto> dtoList = result.getContent().stream()
                .map(cat -> CategoryDto.toDto(cat, Language.VI.getCode()))
                .toList();

        model.addAttribute("page", result);
        model.addAttribute("categories", dtoList); // DTO thay vì entity
        model.addAttribute("q", q);

        return "admin/category/list";
    }


    @GetMapping({"/create", "/edit/{id}"})
    public String showForm(@PathVariable(required = false) String id, Model model) {

        List<LanguageDto> languageOptions = Arrays.stream(Language.values())
                .map(LanguageDto::from)
                .toList();
        model.addAttribute("languages", languageOptions);

        if (id == null) {
            // 🔹 Tạo mới
            CategoryDto dto = new CategoryDto();
            model.addAttribute("category", dto);
            return "admin/category/form";
        }

        try {
            Optional<Category> categoryOpt = categoryService.findById(id);
            if (categoryOpt.isEmpty()) {
                return "redirect:/admin/categories";
            }

            Category category = categoryOpt.get();

            String defaultLang = Language.VI.getCode();
            String nameDefault = category.getNameByLanguage(defaultLang);
            String descDefault = category.getDescriptionByLanguage(defaultLang);

            List<TranslationEntry> translations = new ArrayList<>();
            for (LocalizedText nameEntry : category.getName()) {
                if (nameEntry.getLanguage().equalsIgnoreCase(defaultLang)) continue;

                TranslationEntry entry = new TranslationEntry();
                entry.setLanguage(nameEntry.getLanguage());
                entry.setName(nameEntry.getValue());

                category.getDescription().stream()
                        .filter(desc -> desc.getLanguage().equalsIgnoreCase(nameEntry.getLanguage()))
                        .findFirst()
                        .ifPresent(desc -> entry.setDescription(desc.getValue()));

                translations.add(entry);
            }

            CategoryDto dto = CategoryDto.builder()
                    .id(category.getId())
                    .name(nameDefault)
                    .description(descDefault)
                    .translations(translations)
                    .build();

            model.addAttribute("category", dto);
            model.addAttribute("translations", translations);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/admin/categories";
        }

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


