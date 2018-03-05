package com.vanya.controller;

import com.vanya.dto.RegistrationUserDto;
import com.vanya.dto.UserDto;
import com.vanya.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.websocket.server.PathParam;

import java.util.Optional;

@RestController
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/api/user/{userName}")
    public UserDto getUserDetails(@PathVariable String userName) {
        Optional<UserDto> userByName = userService.getUserByName(userName);
        return userByName.orElse(null);
    }


    @PostMapping("/api/user/")
    public ModelAndView createNewUser(@Valid @ModelAttribute("userForm") RegistrationUserDto user,
                                      HttpServletRequest request) {
        log.info("Got user registration request: {}", user.getEmail());
        userService.validateNewUser(user);

        if (!user.getPassword().equals(user.getConfirmPassword())) {
            throw new IllegalArgumentException(" The password and confirm password should be the same");
        }

        if (StringUtils.isBlank(request.getParameter("g-recaptcha-response"))) {
            throw new IllegalArgumentException("Click on capcha");
        }
        userService.registryNewUser(user);

        return new ModelAndView("redirect:/login");
    }

    @RequestMapping("/api/user/registration/resend/")
    public String resendRegistrationToken(@PathParam("email") String email) {
        if (userService.isConfirmedEmail(email)) {
            return "You've already confirmed registration";
        } else {
            userService.resendRegistrationToken(email);
        }
        return "Success";
    }
}
