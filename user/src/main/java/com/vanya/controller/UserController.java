package com.vanya.controller;

import com.vanya.dto.*;
import com.vanya.service.FriendsService;
import com.vanya.service.PhotoService;
import com.vanya.service.UserService;
import com.vanya.utils.Pager;
import com.vanya.utils.PaginationUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.websocket.server.PathParam;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PhotoService photoService;

    @Autowired
    private FriendsService friendsService;

    @GetMapping("/api/user")
    public ResponseEntity<?> getAllUsers(@RequestParam("page") Optional<Integer> page,
                                         @RequestParam("pageSize") Optional<Integer> pageSize,
                                         @RequestParam("usernameLike") Optional<String> userNameLike) {
        int evalPageSize = pageSize.orElse(PaginationUtils.INITIAL_PAGE_SIZE);
        int evalPage = (page.orElse(0) < 1) ? PaginationUtils.INITIAL_PAGE_SIZE : page.get() - 1;

        final PageRequest pageRequest = new PageRequest(evalPage, evalPageSize);
        final Page<UserDto> users = getUsers(userNameLike, pageRequest);
        final Pager pager = new Pager(users.getTotalPages(), users.getNumber(), PaginationUtils.BUTTONS_TO_SHOW);
        final PagebleUserDTO response = new PagebleUserDTO();
        long currentUserId = userService.getCurrentUserId();

        List<Long> friends = users.getContent()
                .stream()
                .map(user -> {
                    if (friendsService.isFriend(currentUserId, user.getId())) {
                        return user.getId();
                    } else {
                        return -1L;
                    }
                })
                .collect(Collectors.toList());

        response.setEvalPage(evalPage);
        response.setEvalPageSize(evalPageSize);
        response.setUsers(users);
        response.setPager(pager);

        response.setFriends(friends);
        return ResponseEntity.ok(response);
    }

    private Page<UserDto> getUsers(Optional<String> userNameLike, PageRequest pageRequest) {
        if (userNameLike.isPresent() && !StringUtils.isBlank(userNameLike.get())) {
            return userService.findAllPagebleLike(userNameLike.get(), pageRequest);
        }
        return userService.findAllPageable(pageRequest);
    }

    @GetMapping("/api/user/{userId}")
    public ResponseEntity<?> getUser(@PathVariable("userId") long userId) {
        final UserDto user = userService.getUser(userId).orElseThrow(RuntimeException::new);
        final UserDto currentUser = userService.getUserByName(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(RuntimeException::new);//todo replace on only getIdmethod

        final boolean isFriend = friendsService.isFriend(currentUser.getId(), userId);
        final UserProfileDto response = new UserProfileDto();
        response.setFriend(isFriend);
        response.setUserDto(user);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/user/{userName}/information")
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
    public String changeUserPassword(@Valid @ModelAttribute("userForm") ChangePasswordDTO passwordDTO) {
        log.info("Got change password  request: {}", passwordDTO.getToken());
        if (!userService.isValidChangePasswordToken(passwordDTO.getToken())) {
            return "You can't change password";
        } else if (!passwordDTO.getPassword().equals(passwordDTO.getConfirmPassword())) {
            return "Password and confirm password must be the same";
        }

        userService.changeUserPasswordViaToken(passwordDTO);
        return "Success";
    }

    @PostMapping("/api/user/{userId}/password")
    public void changeUserPassword(@PathVariable long userId, @Valid UpdatePasswordDTO newPassword) {
        userService.changeUserPassword(newPassword, userId);
    }

    @PostMapping("/api/user/{userId}/photo")
    public ResponseEntity<?> changeUserPhoto(@RequestParam("uploadfile") MultipartFile uploadfile,
                                             @PathVariable long userId) {
        try {
            if (uploadfile.getSize() < 100) {
//todo            make this check on server
                return new ResponseEntity<>(HttpStatus.OK);
            }
            photoService.storeNewPhoto(userId, uploadfile);
        } catch (IOException e) {
            e.printStackTrace();
            new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/api/user/{userId}/photo/default")
    public ResponseEntity<?> setDefaultPathToPhoto(@PathVariable long userId) {
        userService.setNewPhoto(userId, "anonymous.png");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/api/user/{userId}")
    public ResponseEntity<?> changeCurrentUser(@PathVariable long userId,
                                               UpdateUserDto newUser) {
        userService.changeUser(userId, newUser);
        return new ResponseEntity<>(HttpStatus.OK);
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

    @GetMapping("/api/user/current")
    public UserDto getCurrentUser() {
        String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.getCurrentUser(currentUserName);
    }

}
