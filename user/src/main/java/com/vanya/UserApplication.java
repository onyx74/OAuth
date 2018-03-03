package com.vanya;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

/**
 * @author Ivan Hladush
 */
@SpringBootApplication
@EnableResourceServer
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.vanya.client")
@EnableEurekaClient
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableOAuth2Client
public class UserApplication   extends ResourceServerConfigurerAdapter {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().formLogin().loginPage("/login").permitAll().and().
                authorizeRequests().antMatchers("/reader").permitAll();

    }
}
