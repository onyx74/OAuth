package com.vanya.controller;

import com.vanya.client.UserClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @Autowired
    private UserClient userClient;


//    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping({"/api/user/{userId}"})
    public String reader(@PathVariable Long userId) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getName());

        return "READER" + userId;
    }

    @RequestMapping({"/api/user/writer"})
    public String writer() {

        System.out.println(SecurityContextHolder.getContext().getAuthentication().getName());
        final String writer = userClient.getWriter(1);
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getName());
        return "WRITER"+writer;
    }
}
