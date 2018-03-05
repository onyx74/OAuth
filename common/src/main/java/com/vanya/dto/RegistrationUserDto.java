package com.vanya.dto;

import com.vanya.validation.annotations.ValidEmail;
import com.vanya.validation.annotations.ValidPassword;
import com.vanya.validation.annotations.ValidUserName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationUserDto {
    @ValidUserName
    private String username;

    @ValidPassword
    private String password;

    @ValidEmail
    private String email;

    private String confirmPassword;
}
