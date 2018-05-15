package com.vanya.controller;

import com.vanya.dto.TruckDTO;
import com.vanya.dto.pageble.PagebleLoadsDTO;
import com.vanya.dto.pageble.PagebleTruckDTO;
import com.vanya.service.TruckService;
import com.vanya.utils.Pager;
import com.vanya.utils.PaginationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.vanya.utils.PaginationUtils.getEvalPage;

@Controller
public class TruckController {
    @Autowired
    private TruckService truckService;


    @GetMapping("/api/trucks/{truckId}")
    public ResponseEntity<?> getTruck(@PathVariable("truckId") Long truckId) {
        return ResponseEntity.ok(truckService.getTruck(truckId));
    }

    @PostMapping("/api/trucks/{truckId}")
    public ResponseEntity<?> editTruck(@PathVariable("truckId") Long truckId, TruckDTO truckDTO) {
        return ResponseEntity.ok(truckService.addNewTruck(truckDTO));

    }

    @PostMapping("/api/trucks")
    public ResponseEntity<?> createNewTruck(TruckDTO truckDTO) {
        return ResponseEntity.ok(truckService.addNewTruck(truckDTO));

    }

    @DeleteMapping("/api/trucks/{truckId}")
    public ResponseEntity<?> deleteTruck(@PathVariable long truckId) {
        truckService.removeTruck(truckId);
        return ResponseEntity.ok("");
    }

    @GetMapping("/api/trucks/current")
    public ResponseEntity<?> getAllTrucksForCurrentUser(@RequestParam("page") Optional<Integer> page,
                                                        @RequestParam("pageSize") Optional<Integer> pageSize,
                                                        @RequestParam("carModel") String carModel,
                                                        @RequestParam("position") String position) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        int evalPage = getEvalPage(page);
        int evalPageSize = pageSize.orElse(PaginationUtils.INITIAL_PAGE_SIZE);

        final Page<TruckDTO> loadEntities = truckService.findAllTrucks(username, carModel, position, evalPage, evalPageSize);
        final PagebleTruckDTO response = getPagebleTruckDTO(evalPageSize, evalPage, loadEntities);
        return ResponseEntity.ok(response);
    }


    private PagebleTruckDTO getPagebleTruckDTO(int evalPageSize, int evalPage, Page<TruckDTO> truckDTOS) {
        final Pager pager = new Pager(truckDTOS.getTotalPages(),
                truckDTOS.getNumber(),
                PaginationUtils.BUTTONS_TO_SHOW);

        final PagebleTruckDTO response = new PagebleTruckDTO();
        response.setEvalPage(evalPage);
        response.setEvalPageSize(evalPageSize);
        response.setTrucks(truckDTOS);
        response.setPager(pager);
        response.setNumber(truckDTOS.getNumber());
        return response;
    }
}
