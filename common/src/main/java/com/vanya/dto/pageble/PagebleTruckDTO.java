package com.vanya.dto.pageble;

import com.vanya.dto.TruckDTO;
import com.vanya.utils.Pager;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
public class PagebleTruckDTO {
    private Page<TruckDTO> trucks;
    private int evalPageSize;
    private Pager pager;
    private int evalPage;
    private int number;
}
