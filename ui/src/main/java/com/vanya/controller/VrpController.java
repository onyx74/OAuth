package com.vanya.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VrpController {
    @GetMapping("/newVrpRoad")
    public String getNewVrpRoad() {
        return "/vrp/createNewVrp";
    }

    @GetMapping("/allVrpRoad")
    public String getAllVrpRoad() {
        return "/vrp/allVrp";
    }
}
