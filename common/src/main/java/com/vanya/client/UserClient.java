package com.vanya.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "user-service")
public interface UserClient {

    @RequestMapping(value = "/api/user/{userId}", method = RequestMethod.GET)
    String getWriter(@PathVariable("userId") long userId);
}
