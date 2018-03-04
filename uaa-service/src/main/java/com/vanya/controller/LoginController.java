package com.vanya.controller;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.security.PermitAll;

@Controller
public class LoginController {

    @GetMapping("/login")
    @PermitAll
    public String loginPage(@RequestParam(required = false, name = "error") String error, Model model) {
        if (!StringUtils.isBlank(error)) {
            model.addAttribute("error", error);
        }
        return "login";
    }
}