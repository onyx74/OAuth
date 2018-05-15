package com.vanya.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class LoadController {

    @GetMapping("/allLoads")
    public String getLoadsPage() {
        return "/loads/allLoads";
    }

    @GetMapping("/myLoads")
    public String getMyLoads() {
        return "/loads/myLoads";
    }

    @GetMapping("/newLoad")
    public String getCreateLoad() {
        return "/loads/createLoad";
    }

    @GetMapping("/loadDetails")
    public String getLoadDetails() {
        return "/loads/loadDetails";
    }

    @GetMapping("/editLoad")
    public String getEditLoad() {
        return "/loads/editLoad";
    }

    @GetMapping("/loads/{loadId}")
    public String getLoad(@PathVariable("loadId") Long loadId) {
        return "/loads/load";
    }


    @GetMapping("/loads/{loadId}/private")
    public String getPrivateLoad(@PathVariable("loadId") Long loadId) {
        return "/loads/loadPrivate";
    }

    @GetMapping("/load/edit/{loadId}")
    public String getEditLoad(@PathVariable("loadId") Long loadId) {
        return "/loads/editLoad";
    }
}
