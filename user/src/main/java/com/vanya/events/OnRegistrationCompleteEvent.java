package com.vanya.events;

import com.vanya.model.UserEntity;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString
public class OnRegistrationCompleteEvent extends ProjectEvent {

    private final String appUrl;
    private final UserEntity user;
    private final String token;

    public OnRegistrationCompleteEvent(UserEntity user, String appUrl) {
        super(user);
        this.user = user;
        this.appUrl = appUrl;
        this.token = UUID.randomUUID().toString();
    }

    public String getSubject() {
        return "Registration Confirmation";
    }

    public String getMessageForUser() {
        return String.format("Thank you for registering on our website." +
                " \r\n %s/api/user/registration/confirm?token=%s", appUrl, token);


    }
}
