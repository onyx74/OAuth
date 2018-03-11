package com.vanya.events;

import com.vanya.model.UserEntity;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString
public class OnChangePasswordEvent extends ProjectEvent {

    private final String appUrl;
    private final UserEntity user;
    private final String token;

    public OnChangePasswordEvent(UserEntity user, String appUrl) {
        super(user);
        this.user = user;
        this.appUrl = appUrl;
        this.token = UUID.randomUUID().toString();
    }

    public String getSubject() {
        return "Registration Confirmation";
    }

    public String getMessageForUser() {
        return String.format("You request for change password." +
                                     " \r\n %s/api/user/password?token=%s", appUrl, token);


    }
}
