package com.vanya.dto;

import com.vanya.validation.annotations.ValidPassword;
import lombok.Data;
import lombok.ToString;

@Data
public class UpdatePasswordDTO {

    @ValidPassword
    private String password;

    private String confirmPassword;
}
