package com.vanya.controller;

import com.vanya.dto.LoadDTO;
import com.vanya.dto.UserDto;
import com.vanya.dto.pageble.PagebleFriendsDTO;
import com.vanya.dto.pageble.PagebleLoadsDTO;
import com.vanya.service.LoadService;
import com.vanya.utils.Pager;
import com.vanya.utils.PaginationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.vanya.utils.PaginationUtils.getEvalPage;

@RestController
public class LoadController {

    @Autowired
    private LoadService loadService;

    @GetMapping("/api/loads/test")
    public String test() {
        return "getTest";
    }

    @GetMapping("/api/loads/{loadId}")
    public ResponseEntity<?> getLoad(@PathVariable("loadId") Long loadId) {
        return ResponseEntity.ok(loadService.getLoad(loadId));
    }

    @PostMapping("/api/loads/{loadId}")
    public ResponseEntity<?> editLoad(@PathVariable("loadId") Long loadId, LoadDTO loadDTO) {
        return ResponseEntity.ok(loadService.addNewLoad(loadDTO));

    }

    @PostMapping("/api/loads")
    public ResponseEntity<?> createNewLoad(LoadDTO loadDTO) {
        return ResponseEntity.ok(loadService.addNewLoad(loadDTO));

    }

    @DeleteMapping("/api/loads/{loadId}")
    public ResponseEntity<?> deleteLoad(@PathVariable long loadId) {
        loadService.removeLoad(loadId);
        return ResponseEntity.ok("");
    }

    @GetMapping("/api/loads/current")
    public ResponseEntity<?> getAllLoadsForCurrentUser(@RequestParam("page") Optional<Integer> page,
                                                       @RequestParam("pageSize") Optional<Integer> pageSize) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        int evalPage = getEvalPage(page);
        int evalPageSize = pageSize.orElse(PaginationUtils.INITIAL_PAGE_SIZE);

        final Page<LoadDTO> loadEntities = loadService.findAllLoads(username, evalPage, evalPageSize);
        final PagebleLoadsDTO response = getPagebleLoadsDTO(evalPageSize, evalPage, loadEntities);
        return ResponseEntity.ok(response);
    }


    private PagebleLoadsDTO getPagebleLoadsDTO(int evalPageSize, int evalPage, Page<LoadDTO> loadEntities) {
        final Pager pager = new Pager(loadEntities.getTotalPages(),
                loadEntities.getNumber(),
                PaginationUtils.BUTTONS_TO_SHOW);

        final PagebleLoadsDTO response = new PagebleLoadsDTO();
        response.setEvalPage(evalPage);
        response.setEvalPageSize(evalPageSize);
        response.setLoads(loadEntities);
        response.setPager(pager);
        response.setNumber(loadEntities.getNumber());
        return response;
    }
}
