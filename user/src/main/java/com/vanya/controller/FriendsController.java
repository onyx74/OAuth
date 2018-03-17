package com.vanya.controller;

import com.vanya.service.FriendsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class FriendsController {
    @Autowired
    private FriendsService friendsService;

    @PostMapping("/api/user/{userId}/friends/{friendId}")
    public ResponseEntity<?> addNewFriend(@PathVariable long userId, @PathVariable long friendId) {
        return ResponseEntity.ok(friendsService.addNewFriend(userId, friendId));

    }

    @DeleteMapping("/api/user/{userId}/friends/{friendId}")
    public ResponseEntity<?> removeFriend(@PathVariable long userId, @PathVariable long friendId) {
        return ResponseEntity.ok(friendsService.removeFriend(userId, friendId));

    }
}
