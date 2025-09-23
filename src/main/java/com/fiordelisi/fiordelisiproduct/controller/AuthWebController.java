package com.fiordelisi.fiordelisiproduct.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AuthWebController {
    @GetMapping("/admin-login")
    public String login() {
        return "login";
    }
}
