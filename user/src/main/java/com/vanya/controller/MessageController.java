package com.vanya.controller;

import com.vanya.dto.MessageDTO;
import com.vanya.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {
    @Autowired
    private MessageService messageService;

    @PostMapping("/api/user/{userId}/messages")
    public ResponseEntity<?> addNewMessage(@PathVariable long userId, MessageDTO newMessage) {
        return ResponseEntity.ok(messageService.addNewMessage(userId, newMessage));
    }
}
