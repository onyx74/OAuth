package com.vanya.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Comparator;
import java.util.TreeMap;
import java.util.TreeSet;

@Controller
public class MapController {
    @GetMapping("/loadsMap")
    public String getLoadMap() {
        return "/map/loadsMap";
    }

    @GetMapping("/vrpMap/{vrpId}")
    public String getVrpMap(@PathVariable long vrpId) {
        return "/map/vrpMap";
    }

    @GetMapping("loadMap/{loadId}")
    public String getLoadOnMap(@PathVariable long loadId) {
        return "/map/loadMap";
    }

    @GetMapping("/myLoadsMap")
    public String getMyLoadMap() {
        return "/map/myLoadsMap";
    }

    @GetMapping("/myTrucksMap")
    public String getMyTruckMap() {
        return "/map/myTrucksMap";
    }

    @GetMapping("/allTrucksMap")
    public String getAllTrucksMap() {
        return "/map/allTrucksMap";
    }

    @GetMapping("/allLoadsMap")
    public String getAllLoadMap() {
        return "/map/allLoadsMap";
    }
}
