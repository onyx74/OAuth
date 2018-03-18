package com.vanya.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MessageController {
    @RequestMapping("/sendMessage/{userId}")
    public String getSendMessagePage(@PathVariable long userId) {
        return "/messages/sendMessage";
    }

    @RequestMapping("/sendMessage")
    public String getSendMessagePage() {
        return "/messages/sendMessage";
    }
}
