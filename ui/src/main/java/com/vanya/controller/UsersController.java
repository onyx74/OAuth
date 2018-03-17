package com.vanya.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UsersController {
    @RequestMapping("/users")
    public String getUsers() {
        return "/users/users";
    }


    @RequestMapping("/users/{userId}")
    public String getUser(@PathVariable("userId") long userId) {
        return "/users/userProfile";
    }
}
