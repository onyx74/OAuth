package com.vanya.dto;

import com.vanya.utils.Pager;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class PagebleFriendsDTO {
    private List<UserDto> users;
    private int evalPageSize;
    private Pager pager;
    private int evalPage;
    private int number;
}
