package com.vanya.dto;

import com.vanya.validation.annotations.ValidPassword;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordDTO {

    @ValidPassword
    private String password;

    @ValidPassword
    private String confirmPassword;

    private String token;

}
