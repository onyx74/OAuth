package com.vanya.dto.pageble;

import com.vanya.dto.MessageDTO;
import com.vanya.utils.Pager;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.domain.Page;


@Data
@ToString
public class PagebleMessageDTO {
    private Page<MessageDTO> messages;
    private int evalPageSize;
    private Pager pager;
    private int evalPage;
    private int number;
}
