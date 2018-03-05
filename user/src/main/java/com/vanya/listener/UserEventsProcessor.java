package com.vanya.listener;

import com.vanya.events.OnChangePasswordEvent;
import com.vanya.events.OnRegistrationCompleteEvent;
import com.vanya.model.UserEntity;
import com.vanya.service.UserService;
import com.vanya.utils.EmailSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserEventsProcessor {
    @Autowired
    private UserService service;

    @Autowired
    private EmailSender emailSender;


    void processChangePasswordEvent(OnChangePasswordEvent event) {
        final UserEntity user = event.getUser();

        service.createChangePasswordTokenForUser(user, event.getToken());
        emailSender.sendMessage(user.getEmail(), event.getMessageForUser(), event.getSubject());

        log.info("Change password notification email was sent successfully. User email is:{}", event.getUser().getEmail());
    }

    public void processConfirmRegistrationEvent(final OnRegistrationCompleteEvent event) {
        final UserEntity user = event.getUser();

        service.createVerificationTokenForUser(user, event.getToken());
        emailSender.sendMessage(user.getEmail(), event.getMessageForUser(), event.getSubject());

        log.info("Registration confirm email was sent successfully. User email is:{}", event.getUser().getEmail());
    }

}
