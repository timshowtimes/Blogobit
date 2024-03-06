package com.timinsquare.blogobit2.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/")
public class AuthController {

    @GetMapping()
    public String home() {
        return "home";
    }
}
