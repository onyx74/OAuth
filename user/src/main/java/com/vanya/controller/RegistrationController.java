package com.vanya.controller;

import com.vanya.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.websocket.server.PathParam;

@Slf4j
@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;

    @RequestMapping("/api/user/registration/confirm")
    public String confirmRegistration(@PathParam("token") String token, Model model) {
        final String result = userService.confirmUserRegistration(token);
        model.addAttribute("result", result);
        return "/success-registration-confirm";
    }

    @RequestMapping("/api/user/password")
    public String changePassword(@PathParam("token") String token, Model model) {
        model.addAttribute("token", token);
        return "/changePasswordPage";
    }

    @RequestMapping("/api/user/registration/success")
    public String registrationCompleted() {
        return "/success-registration";
    }

    @RequestMapping("api/user/resendRegistrationToken")
    public String resendRegistrationToken() {
        return "resendRegistrationToken";
    }

    @RequestMapping("/api/user/resendRegistrationToken/success")
    public String resendRegistrationTokenSuccess() {
        return "success-resend-token";
    }

    @RequestMapping("/api/user/forgotPassword")
    public String forgotPasswordPage() {
        return "forgotPasswordPage";
    }

    @RequestMapping("/api/user/success/sendChangePassword")
    public String sendChangePasswordSuccess() {
        return "sendChangePasswordSuccess";
    }

    @RequestMapping("/api/user/changePassword/success")
    public String changePasswordSuccess() {
        return "changePasswordSuccess";
    }
}


