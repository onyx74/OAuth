package com.vanya.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class MapController {
    @GetMapping("/loadsMap")
    public String getLoadMap() {
        return "/map/loadsMap";
    }

    @GetMapping("loadMap/{loadId}")
    public String getLoadOnMap(@PathVariable long loadId) {
        return "/map/loadMap";
    }

    @GetMapping("/myLoadsMap")
    public String getMyLoadMap() {
        return "/map/myLoadsMap";
    }
}
