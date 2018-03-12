package com.vanya.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String getGreeting() {
        return "index";
    }

    @GetMapping("/error")
    public String getError() {
        return "error";
    }

    @GetMapping("/profile")
    public String getProfile() {
        return "/profile/profile";
    }

    @GetMapping("/changePassword")
    public String getChangePassword() {
        return "/profile/changePassword";
    }

    @GetMapping("/editProfile")
    public String getEditProfile() {
        return "/profile/editProfile";
    }
}
