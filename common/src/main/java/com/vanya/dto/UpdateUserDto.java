package com.vanya.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.vanya.validation.annotations.ValidEmail;
import lombok.Data;
import lombok.ToString;

import java.sql.Date;

@Data
public class UpdateUserDto {
    @ValidEmail
    private String email;

    private String firstName;
    private String surname;
    private String phoneNumber;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date birthDate;
}