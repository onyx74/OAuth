package com.vanya;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

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
@PropertySource({"email.properties"})
@ComponentScan({"com.vanya.*", "com.vanya.validation.validators"})
public class UserApplication extends ResourceServerConfigurerAdapter {

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.formLogin().loginPage("/login").permitAll()
            .and()
            .authorizeRequests()
            .antMatchers("/reader", "/api/user/{userName}","/**",
                         "/api/user/registration/success", "/api/user", "/api/user/css/**", "/api/user/js/**"
                    , "/api/user/registration/resend/**").permitAll()
            .and()
            .logout()
            .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
            .logoutSuccessUrl("/");

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
