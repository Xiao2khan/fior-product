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

    @GetMapping({"vi/cart","en/cart"})
    public String cart(){
        return "/website/cart";
    }

    @GetMapping({"vi/placeOrder","en/placeOrder"})
    public String order(){
        return "/website/placeOrder";
    }

    @GetMapping({"vi/placeOrder/success","en/placeOrder/success"})
    public String success(){
        return "/website/success";
    }

    @GetMapping({"vi/contact","en/contact"})
    public String contact(){
        return "/website/contact";
    }

    @GetMapping({"vi/contact-us","en/contact-us"})
    public String contactUs(){
        return "/website/contact-us";
    }
    @GetMapping({"vi/contact-us/success","en/contact-us/success"})
    public String contactUsSuccess(){
        return "/website/contact-success";
    }
}


