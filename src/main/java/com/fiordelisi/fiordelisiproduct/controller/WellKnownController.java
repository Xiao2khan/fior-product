package com.fiordelisi.fiordelisiproduct.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WellKnownController {

    @GetMapping("/.well-known/**")
    public ResponseEntity<Void> wellKnownNoop() {
        return ResponseEntity.noContent().build();
    }
}


