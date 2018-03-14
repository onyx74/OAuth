package com.vanya.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UsersController {
    @RequestMapping("/users")
    public String getUsers() {
        return "/users/users";
    }
}
