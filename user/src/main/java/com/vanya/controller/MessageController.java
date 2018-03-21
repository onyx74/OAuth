package com.vanya.controller;

import com.vanya.dto.MessageDTO;
import com.vanya.dto.pageble.PagebleMessageDTO;
import com.vanya.service.MessageService;
import com.vanya.service.UserService;
import com.vanya.utils.Pager;
import com.vanya.utils.PaginationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class MessageController {
    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @PostMapping("/api/user/{userId}/messages")
    public ResponseEntity<?> addNewMessage(@PathVariable long userId, MessageDTO newMessage) {
        return ResponseEntity.ok(messageService.addNewMessage(userId, newMessage));
    }

    @GetMapping("/api/user/{userId}/messagesSent")
    public ResponseEntity<?> getAllSentMessages(@PathVariable("userId") long userId,
                                                @RequestParam("page") Optional<Integer> page,
                                                @RequestParam("pageSize") Optional<Integer> pageSize) {
        int evalPageSize = pageSize.orElse(PaginationUtils.INITIAL_PAGE_SIZE);

        int evalPage = (page.orElse(0) < 1) ? PaginationUtils.INITIAL_PAGE_SIZE : page.get() - 1;

        final PageRequest pageRequest = new PageRequest(evalPage, evalPageSize, new Sort(Sort.Direction.DESC, "createdAt"));
        final Page<MessageDTO> messages = messageService.getAllMessages(userId, pageRequest);
        final Pager pager = new Pager(messages.getTotalPages(),
                messages.getNumber(),
                PaginationUtils.BUTTONS_TO_SHOW);

        //todo create method for create response
        final PagebleMessageDTO response = new PagebleMessageDTO();
        response.setEvalPage(evalPage);
        response.setEvalPageSize(evalPageSize);
        response.setMessages(messages);
        response.setPager(pager);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/user/current/messagesSent")
    public ResponseEntity<?> getAllSentMessages(@RequestParam("page") Optional<Integer> page,
                                                @RequestParam("pageSize") Optional<Integer> pageSize) {
        long currentUserId = userService.getCurrentUserId();
        return getAllSentMessages(currentUserId, page, pageSize);
    }

    @GetMapping("/api/user/current/messages/{messageId}")
    public ResponseEntity<?> getMessage(@PathVariable("messageId") long messageId) {
        return ResponseEntity.ok(messageService.getMessage(messageId));
    }

    @GetMapping("/api/user/{userId}/messages")
    public ResponseEntity<?> getAllMessages(@PathVariable("userId") long userId,
                                            @RequestParam("page") Optional<Integer> page,
                                            @RequestParam("pageSize") Optional<Integer> pageSize,
                                            @RequestParam("subject") String subject,
                                            @RequestParam("sentTo") String sentTo) {
        int evalPage = (page.orElse(0) < 1) ? PaginationUtils.INITIAL_PAGE_SIZE : page.get() - 1;
        int evalPageSize = pageSize.orElse(PaginationUtils.INITIAL_PAGE_SIZE);


        final PageRequest pageRequest = new PageRequest(evalPage, evalPageSize, new Sort(Sort.Direction.DESC, "createdAt"));

        final Page<MessageDTO> messages = messageService.getAllMessages(userService.getCurrentUsername(), pageRequest, subject, sentTo);

        final Pager pager = new Pager(messages.getTotalPages(),
                messages.getNumber(),
                PaginationUtils.BUTTONS_TO_SHOW);

        //todo create method for create response
        final PagebleMessageDTO response = new PagebleMessageDTO();
        response.setEvalPage(evalPage);
        response.setEvalPageSize(evalPageSize);
        response.setMessages(messages);
        response.setPager(pager);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/user/current/messages")
    public ResponseEntity<?> getAllMessages(@RequestParam("page") Optional<Integer> page,
                                            @RequestParam("pageSize") Optional<Integer> pageSize,
                                            @RequestParam("subject") String subject,
                                            @RequestParam("sentTo") String sentTo) {
        long currentUserId = userService.getCurrentUserId();
        return getAllMessages(currentUserId, page, pageSize, subject, sentTo);
    }

    @PostMapping("/api/user/current/message/{messageId}")
    public ResponseEntity<?> readMessage(@PathVariable("messageId") long messageId) {
        messageService.readMessage(messageId);
        return ResponseEntity.ok("");
    }

    @GetMapping("/api/user/current/unreadMessages")
    public ResponseEntity<?> getCountOfUnreadMessages() {
        String username = userService.getCurrentUsername();
        return ResponseEntity.ok(messageService.getCountOfUnreadMessages(username));
    }
}
//todo check that Id the same ;
