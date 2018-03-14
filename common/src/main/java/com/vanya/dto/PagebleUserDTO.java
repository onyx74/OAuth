package com.vanya.dto;

import com.vanya.utils.Pager;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.domain.Page;

@Data
@ToString
public class PagebleUserDTO {
    private Page<UserDto> users;
    private int evalPageSize;
    private Pager pager;
    private int evalPage;
}
