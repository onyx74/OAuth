package com.vanya.dto.pageble;

import com.vanya.dto.LoadDTO;
import com.vanya.utils.Pager;
import lombok.Data;
import org.springframework.data.domain.Page;


@Data
public class PagebleLoadsDTO {
    private Page<LoadDTO> loads;
    private int evalPageSize;
    private Pager pager;
    private int evalPage;
    private int number;
}
