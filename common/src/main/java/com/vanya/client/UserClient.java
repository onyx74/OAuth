package com.vanya.client;

import com.vanya.dto.UserDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "user-service")
public interface UserClient {

    @RequestMapping(value = "/api/user/{userName}/information", method = RequestMethod.GET)
    UserDto getUserDto(@PathVariable("userName") String userName);
}
