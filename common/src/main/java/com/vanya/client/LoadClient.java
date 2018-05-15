package com.vanya.client;

import com.vanya.dto.LoadDTO;
import com.vanya.dto.UserDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "load-service")
public interface LoadClient {
    @RequestMapping(value = "/api/loads/info", method = RequestMethod.GET)
    List<LoadDTO> getLoads(@RequestParam("loadsId[]") List<Long> loadsId);
}
