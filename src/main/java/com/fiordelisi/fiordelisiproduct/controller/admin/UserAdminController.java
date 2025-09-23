package com.fiordelisi.fiordelisiproduct.controller.admin;

import com.fiordelisi.fiordelisiproduct.controller.BaseController;
import com.fiordelisi.fiordelisiproduct.dto.UserDto;
import com.fiordelisi.fiordelisiproduct.service.UserService;
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
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class UserAdminController extends BaseController {

    private final UserService userService;

    @GetMapping
    public String list(@RequestParam(value = "q", required = false) String q,
                       @RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "size", defaultValue = "10") int size,
                       Model model) {
        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.max(size, 1), Sort.by(Sort.Order.asc("username")));
        Page<com.fiordelisi.fiordelisiproduct.entity.User> result = userService.search(q, pageable);
        model.addAttribute("page", result);
        model.addAttribute("users", result.getContent());
        model.addAttribute("q", q);
        return "admin/user/list";
    }

    @GetMapping({"/create", "/edit/{id}"})
    public String form(@PathVariable(required = false) String id, Model model) {
        UserDto dto = userService.getUserDtoForForm(id);
        model.addAttribute("user", dto);
        return "admin/user/form";
    }

    @PostMapping({"/create", "/edit/{id}"})
    public String submit(@PathVariable(required = false) String id,
                         @Valid @ModelAttribute("user") UserDto dto,
                         BindingResult bindingResult,
                         RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            return "admin/user/form";
        }
        dto.setId(id);
        userService.saveFromDto(dto);
        ra.addFlashAttribute("success", id == null ? "User created successfully" : "User updated successfully");
        return "redirect:/admin/users";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable String id, RedirectAttributes ra) {
        userService.deleteById(id);
        ra.addFlashAttribute("success", "User deleted successfully");
        return "redirect:/admin/users";
    }
}


