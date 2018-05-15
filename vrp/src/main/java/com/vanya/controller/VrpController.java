package com.vanya.controller;

import com.vanya.service.VrpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class VrpController {

    @Autowired
    public VrpService vrpService;

    @GetMapping("/api/vrp/")
    public ResponseEntity<?> getAllLoadsForCurrentUser(@RequestParam("loads[]") List<Long> loads,
                                                       @RequestParam("startPosition") String startPosition,
                                                       @RequestParam("name") String name) {
        return ResponseEntity.ok(vrpService.calculateOptimalWay(loads, startPosition,name));
    }
}
