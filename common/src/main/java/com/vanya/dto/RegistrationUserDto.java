package com.vanya.dto;

import com.vanya.validation.annotations.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationUserDto {
    @ValidUserName
    private String username;

    @ValidFirstName
    private String firstName;

    @ValidSurname
    private String surname;

    @ValidPassword
    private String password;

    @ValidEmail
    private String email;

    private String confirmPassword;
}
