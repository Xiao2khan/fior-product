package com.fiordelisi.fiordelisiproduct.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootRedirectController {

    @GetMapping({"/", ""})
    public String root() {
        return "redirect:/en";
    }

    @GetMapping({"/en/about","/vi/about"})
    public String about() {
        return "/website/about";
    }

    @GetMapping({"vi/certification","en/certification"})
    public String certification(){
        return "/website/certification";
    }

    @GetMapping({"vi/support","en/support"})
    public String help(){
        return "/website/support";
    }

    @GetMapping({"vi/cart","en/cart"})
    public String cart(){
        return "/website/cart";
    }

    @GetMapping({"vi/order","en/order"})
    public String order(){
        return "/website/order";
    }

    @GetMapping({"vi/order/success","en/order/success"})
    public String success(){
        return "/website/success";
    }

    @GetMapping({"vi/contact","en/contact"})
    public String contact(){
        return "/website/contact";
    }
}


