package com.vanya.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FriendController {
    @RequestMapping("/friends")
    public String getUsers() {
        return "/users/friends";
    }
}
