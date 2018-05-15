package com.vanya.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class TruckController {
    @GetMapping("/allTrucks")
    public String getLoadsPage() {
        return "/trucks/allTrucks";
    }

    @GetMapping("/myTrucks")
    public String getMyLoads() {
        return "/trucks/myTrucks";
    }

    @GetMapping("/newTruck")
    public String getCreateLoad() {
        return "/trucks/createTruck";
    }

    @GetMapping("/truckDetails")
    public String getLoadDetails() {
        return "/trucks/truckDetails";
    }

    @GetMapping("/editTruck")
    public String getEditLoad() {
        return "/trucks/editTruck";
    }

    @GetMapping("/trucks/{truckId}")
    public String getLoad(@PathVariable("truckId") Long truckId) {
        return "/trucks/truck";
    }

    @GetMapping("/truck/edit/{truckId}")
    public String getEditLoad(@PathVariable("truckId") Long truckId) {
        return "/trucks/editTruck";
    }

    @GetMapping("/trucks/{truckId}/private")
    public String getPrivateLoad(@PathVariable("truckId") Long loadId) {
        return "/trucks/truckPrivate";
    }

}
