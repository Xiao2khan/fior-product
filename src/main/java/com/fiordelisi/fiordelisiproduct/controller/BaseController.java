package com.fiordelisi.fiordelisiproduct.controller;

import com.fiordelisi.fiordelisiproduct.config.FiordelisiProductUserPrincipal;
import com.fiordelisi.fiordelisiproduct.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@Slf4j
@ControllerAdvice
public class BaseController {

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Object principal = authentication.getPrincipal();

        if (principal instanceof FiordelisiProductUserPrincipal resellerUserPrincipal) {
            return resellerUserPrincipal.getUser();
        }
        return null;
    }

    @ModelAttribute("currentPath")
    public String getCurrentPath(HttpServletRequest request) {
        return request.getRequestURI();
    }
}
