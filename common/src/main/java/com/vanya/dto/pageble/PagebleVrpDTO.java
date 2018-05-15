package com.vanya.dto.pageble;

import com.vanya.dto.LoadDTO;
import com.vanya.dto.VrpDTO;
import com.vanya.utils.Pager;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
public class PagebleVrpDTO {
    private Page<VrpDTO> vrps;
    private int evalPageSize;
    private Pager pager;
    private int evalPage;
    private int number;
}
