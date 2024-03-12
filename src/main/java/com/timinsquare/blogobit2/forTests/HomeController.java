package com.timinsquare.blogobit2.controllers;

import com.timinsquare.blogobit2.models.BUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/home")
    public String home() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        BUser user = (BUser) authentication.getPrincipal();
        System.out.println("Username is: " + user.getUsername());
        return "home";
    }
}
