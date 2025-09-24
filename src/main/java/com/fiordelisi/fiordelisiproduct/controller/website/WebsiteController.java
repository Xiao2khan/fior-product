package com.fiordelisi.fiordelisiproduct.controller.website;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class WebsiteController {
    @GetMapping("/{lang}")
    public String index(Model model, @PathVariable String lang) {
        return "website/home-page";
    }
}
