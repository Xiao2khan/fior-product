package com.fiordelisi.fiordelisiproduct.controller.admin;

import com.fiordelisi.fiordelisiproduct.controller.BaseController;
import com.fiordelisi.fiordelisiproduct.dto.VariantDto;
import com.fiordelisi.fiordelisiproduct.entity.Product;
import com.fiordelisi.fiordelisiproduct.service.ProductService;
import com.fiordelisi.fiordelisiproduct.service.VariantService;
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

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class VariantAdminController extends BaseController {

    private final ProductService productService;
    private final VariantService variantService;

    @GetMapping("/products/{productId}/variants")
    public String variants(@PathVariable String productId,
                           @RequestParam(value = "q", required = false) String q,
                           @RequestParam(value = "page", defaultValue = "0") int page,
                           @RequestParam(value = "size", defaultValue = "10") int size,
                           Model model) {
        Product product = productService.findById(productId).orElseThrow();
        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.max(size, 1), Sort.by(Sort.Order.asc("sizeGram")));
        Page<com.fiordelisi.fiordelisiproduct.entity.Variant> result = variantService.searchVariantsByProduct(productId, q, pageable);
        model.addAttribute("product", product);
        model.addAttribute("page", result);
        model.addAttribute("variants", result.getContent());
        model.addAttribute("q", q);
        return "admin/variant/list";
    }

    @GetMapping({"/products/{productId}/variants/create", "/products/{productId}/variants/edit/{variantId}"})
    public String variantForm(@PathVariable String productId,
                              @PathVariable(required = false) String variantId,
                              Model model) {
        Product product = productService.findById(productId).orElseThrow();
        VariantDto dto = variantService.getVariantDtoForForm(productId, variantId);
        model.addAttribute("product", product);
        model.addAttribute("variant", dto);
        return "admin/variant/form";
    }

    @PostMapping({"/products/{productId}/variants/create", "/products/{productId}/variants/edit/{variantId}"})
    public String variantSubmit(@PathVariable String productId,
                                @PathVariable(required = false) String variantId,
                                @Valid @ModelAttribute("variant") VariantDto dto,
                                BindingResult bindingResult,
                                RedirectAttributes ra,
                                Model model) {
        if (bindingResult.hasErrors()) {
            Product product = productService.findById(productId).orElseThrow();
            model.addAttribute("product", product);
            return "admin/variant/form";
        }
        variantService.saveVariantFromDto(productId, dto, variantId);
        ra.addFlashAttribute("success", "Variant saved successfully");
        return "redirect:/admin/products/" + productId + "/variants";
    }

    @PostMapping("/products/{productId}/variants/delete/{variantId}")
    public String variantDelete(@PathVariable String productId,
                                @PathVariable String variantId,
                                RedirectAttributes ra) {
        variantService.deleteVariant(productId, variantId);
        ra.addFlashAttribute("success", "Variant deleted successfully");
        return "redirect:/admin/products/" + productId + "/variants";
    }

    // All variants view
    @GetMapping("/variants")
    public String allVariants(@RequestParam(value = "q", required = false) String q,
                              @RequestParam(value = "page", defaultValue = "0") int page,
                              @RequestParam(value = "size", defaultValue = "10") int size,
                              Model model) {
        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.max(size, 1), Sort.by(Sort.Order.asc("sizeGram")));
        Page<com.fiordelisi.fiordelisiproduct.entity.Variant> result = variantService.searchAllVariants(q, pageable);
        model.addAttribute("page", result);
        model.addAttribute("variants", result.getContent());
        model.addAttribute("products", variantService.getAllProducts());
        model.addAttribute("q", q);
        return "admin/variant/all";
    }
}


