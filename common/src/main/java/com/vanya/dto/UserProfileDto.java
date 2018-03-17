package com.vanya.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileDto {
    private UserDto userDto;
    private boolean isFriend;
}
