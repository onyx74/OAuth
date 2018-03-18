package com.vanya.dto.pageble;

import com.vanya.dto.UserDto;
import com.vanya.utils.Pager;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@ToString
public class PagebleUserDTO {
    private Page<UserDto> users;
    private int evalPageSize;
    private Pager pager;
    private int evalPage;
    private List<Long> friends;
}
