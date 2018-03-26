package com.vanya.dto.pageble;

import com.vanya.dto.MessageDTO;
import com.vanya.utils.Pager;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;


@Data
@ToString
public class PagebleMessageDTO {
    private Page<MessageDTO> messages;
    private Map<Long, String> userNames;
    private int evalPageSize;
    private Pager pager;
    private int evalPage;
    private int number;
}
