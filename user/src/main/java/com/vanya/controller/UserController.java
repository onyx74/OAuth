package com.vanya.controller;

import com.vanya.dto.ChangePasswordDTO;
import com.vanya.dto.RegistrationUserDto;
import com.vanya.dto.UserDto;
import com.vanya.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/api/user/test/asd")
    public String testController() {
        return "Test";
    }

    @PostMapping("/api/user/")
    public String createNewUser(@Valid @ModelAttribute("userForm") RegistrationUserDto user,
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

        return "Success";
    }


    @PostMapping("/api/user/changePassword")
    public String changeUserPassword(@Valid @ModelAttribute("userForm") ChangePasswordDTO passwordDTO,
                                     HttpServletRequest request) {
        log.info("Got change password  request: {}", passwordDTO.getToken());
        if (!userService.isValidChangePasswordToken(passwordDTO.getToken())) {
            return "You can't change password";
        } else if (!passwordDTO.getPassword().equals(passwordDTO.getConfirmPassword())) {
            return "Password and confirm password must be the same";
        }

        userService.changeUserPassword(passwordDTO);
        return "Success";
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


    @RequestMapping("/api/user/send/changePasswordToken")
    public String sendChangePasswordToken(@PathParam("email") String email) {
        if (StringUtils.isBlank(email)) {
            return "Please input email";
        } else if (!userService.isEmailExist(email)) {
            return "This email doesn't exist";
        } else if (!userService.isConfirmedEmail(email)) {
            return "You don't confirm your email. You can't change password";
        }

        userService.sendTokenForChangePassword(email);
        return "Success";
    }

}
