package com.vanya.controller;

import com.vanya.dto.PagebleFriendsDTO;
import com.vanya.dto.PagebleUserDTO;
import com.vanya.dto.UserDto;
import com.vanya.model.FriendEntity;
import com.vanya.service.FriendsService;
import com.vanya.service.UserService;
import com.vanya.utils.Pager;
import com.vanya.utils.PaginationUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class FriendsController {
    @Autowired
    private FriendsService friendsService;

    @Autowired
    private UserService userService;

    @GetMapping("/api/user/{userId}/friends")
    public ResponseEntity<?> getAllFriends(@PathVariable("userId") long userId,
                                           @RequestParam("page") Optional<Integer> page,
                                           @RequestParam("pageSize") Optional<Integer> pageSize,
                                           @RequestParam("usernameLike") Optional<String> userNameLike) {
        int evalPageSize = pageSize.orElse(PaginationUtils.INITIAL_PAGE_SIZE);

        int evalPage = (page.orElse(0) < 1) ? PaginationUtils.INITIAL_PAGE_SIZE : page.get() - 1;

        final PageRequest pageRequest = new PageRequest(evalPage, evalPageSize);
        final List<UserDto> users = getFriendsForUser(userId, userNameLike, pageRequest);
        final Page<Long> friendEntities = friendsService.findAllPagebleIds(userId, pageRequest);
        final Pager pager = new Pager(friendEntities.getTotalPages(),
                friendEntities.getNumber(),
                PaginationUtils.BUTTONS_TO_SHOW);

        final PagebleFriendsDTO response = new PagebleFriendsDTO();
        response.setEvalPage(evalPage);
        response.setEvalPageSize(evalPageSize);
        response.setUsers(users);
        response.setPager(pager);
        response.setNumber(friendEntities.getNumber());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/user/current/friends")
    public ResponseEntity<?> getAllFriends(@RequestParam("page") Optional<Integer> page,
                                           @RequestParam("pageSize") Optional<Integer> pageSize,
                                           @RequestParam("usernameLike") Optional<String> userNameLike) {
        long currentUserId = userService.getCurrentUserId();
        return getAllFriends(currentUserId, page, pageSize, userNameLike);
    }

    @PostMapping("/api/user/{userId}/friends/{friendId}")
    public ResponseEntity<?> addNewFriend(@PathVariable long userId, @PathVariable long friendId) {
        return ResponseEntity.ok(friendsService.addNewFriend(userId, friendId));

    }

    @DeleteMapping("/api/user/{userId}/friends/{friendId}")
    public ResponseEntity<?> removeFriend(@PathVariable long userId, @PathVariable long friendId) {
        return ResponseEntity.ok(friendsService.removeFriend(userId, friendId));

    }

    private List<UserDto> getFriendsForUser(long userId, Optional<String> userNameLike, PageRequest pageRequest) {
        if (userNameLike.isPresent() && !StringUtils.isBlank(userNameLike.get())) {
            return friendsService.findAllPagebleLike(userNameLike.get(), pageRequest);
        }
        return friendsService.findAllPageable(userId, pageRequest);
    }

}
