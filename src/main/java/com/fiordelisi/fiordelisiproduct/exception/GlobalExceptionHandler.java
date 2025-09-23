package com.fiordelisi.fiordelisiproduct.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoHandlerFoundException.class)
    public String handleNotFound(NoHandlerFoundException ex, Model model, HttpServletRequest request) {
        String uri = request.getRequestURI();
        // For any unknown route, redirect to default language home
        if (uri == null || uri.isEmpty() || uri.contains("admin")) {
            return "redirect:/en";
        }
        return "redirect:/en";
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex, Model model, HttpServletRequest request) {
        model.addAttribute("errorCode", 500);
        model.addAttribute("errorMessage", "System Error. Please try again later");
        model.addAttribute("path", request.getRequestURI());
        return "error";
    }

}
