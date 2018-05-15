package com.vanya.controller;

import com.vanya.dto.VrpDTO;
import com.vanya.dto.pageble.PagebleVrpDTO;
import com.vanya.service.VrpService;
import com.vanya.utils.Pager;
import com.vanya.utils.PaginationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import static com.vanya.utils.PaginationUtils.getEvalPage;

@RestController
public class VrpController {

    @Autowired
    public VrpService vrpService;

    @GetMapping("/api/vrp/{vrpId}")
    public ResponseEntity<?> getVrp(@PathVariable("vrpId") long vrpId) {
        return ResponseEntity.ok(vrpService.getVrp(vrpId));
    }

    @GetMapping("/api/vrp/current")
    public ResponseEntity<?> getAllLoadsForCurrentUser(@RequestParam("page") Optional<Integer> page,
                                                       @RequestParam("pageSize") Optional<Integer> pageSize,
                                                       @RequestParam("startLocation") String startPosition,
                                                       @RequestParam("name") String name) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        int evalPage = getEvalPage(page);
        int evalPageSize = pageSize.orElse(PaginationUtils.INITIAL_PAGE_SIZE);

        final Page<VrpDTO> vrps = vrpService.findAllVrp(username, startPosition, name, evalPage, evalPageSize);
        final PagebleVrpDTO response = getPagebleVrpDTO(evalPageSize, evalPage, vrps);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/vrp/")
    public ResponseEntity<?> getAllLoadsForCurrentUser(@RequestParam("loads[]") List<Long> loads,
                                                       @RequestParam("startPosition") String startPosition,
                                                       @RequestParam("name") String name) {
        return ResponseEntity.ok(vrpService.calculateOptimalWay(loads, startPosition, name));
    }

    private PagebleVrpDTO getPagebleVrpDTO(int evalPageSize, int evalPage, Page<VrpDTO> vrps) {
        final Pager pager = new Pager(vrps.getTotalPages(),
                vrps.getNumber(),
                PaginationUtils.BUTTONS_TO_SHOW);

        final PagebleVrpDTO response = new PagebleVrpDTO();
        response.setEvalPage(evalPage);
        response.setEvalPageSize(evalPageSize);
        response.setVrps(vrps);
        response.setPager(pager);
        response.setNumber(vrps.getNumber());
        return response;
    }
}
