package com.vanya.controller;

import com.vanya.dto.RegistrationUserDto;
import com.vanya.dto.UserDto;
import com.vanya.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
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

    @GetMapping("/api/user/")
    public String get(){
        return "sdf";
    }
    @PostMapping("/api/user/")
    public ModelAndView createNewUser(@ModelAttribute("userForm") RegistrationUserDto user,
                                      HttpServletRequest request) {
        log.info("Got user registration request: {}", user.getEmail());
        userService.validateNewUser(user);

        if (!user.getPassword().equals(user.getConfirmPassword())) {
            throw new IllegalArgumentException(" The password and confirm password should be the same");
        }

        if (request.getParameter("g-recaptcha-response") == null) {
            throw new IllegalArgumentException("Click on capcha");
        }
        userService.registryNewUser(user);

        return new ModelAndView("redirect:/login");
    }
}
