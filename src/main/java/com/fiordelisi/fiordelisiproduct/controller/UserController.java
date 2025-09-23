package com.fiordelisi.fiordelisiproduct.controller;

import com.fiordelisi.fiordelisiproduct.config.FiordelisiProductUserPrincipal;
import com.fiordelisi.fiordelisiproduct.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/change-password")
    public String changePassword(@RequestParam String currentPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
                                 RedirectAttributes ra) {
        if (!newPassword.equals(confirmPassword)) {
            ra.addFlashAttribute("error", "Confirm password does not match");
            return "redirect:/admin/dashboard";
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();
        if (principal instanceof FiordelisiProductUserPrincipal p) {
            try {
                userService.changePassword(p.getId(), currentPassword, newPassword);
                ra.addFlashAttribute("success", "Password changed successfully");
            } catch (Exception e) {
                ra.addFlashAttribute("error", e.getMessage());
            }
        } else {
            ra.addFlashAttribute("error", "User not authenticated");
        }
        return "redirect:/admin/dashboard";
    }
}


