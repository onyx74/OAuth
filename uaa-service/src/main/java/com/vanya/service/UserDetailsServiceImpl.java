package com.vanya.service;

import com.vanya.client.UserClient;
import com.vanya.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserClient userClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDto credentials = userClient.getUserDto(username);

        if (credentials == null) {
            throw new UsernameNotFoundException(username);
        }
        return credentials;
    }
}
