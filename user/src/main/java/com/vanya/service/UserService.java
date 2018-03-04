package com.vanya.service;

import com.vanya.dao.UserRepository;
import com.vanya.dao.VerificationTokenRepository;
import com.vanya.dto.RegistrationUserDto;
import com.vanya.dto.UserDto;
import com.vanya.events.OnRegistrationCompleteEvent;
import com.vanya.exception.EmailExistException;
import com.vanya.exception.UserNameExistException;
import com.vanya.model.UserEntity;
import com.vanya.model.token.VerificationToken;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${app.url")
    private String appUrl;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;


    public Optional<UserDto> getUserByName(String userName) {
        UserEntity user = userRepository.findUserEntitiesByUsername(userName);
        if (user == null) {
            return Optional.empty();
        }
        return Optional.of(mapper.map(user, UserDto.class));
    }

    public void validateNewUser(RegistrationUserDto user) {
        if (userRepository.findUserEntitiesByUsername(user.getUsername()) != null) {
            throw new UserNameExistException("This user name already exist. Chose other user name");
        }
        if (userRepository.findUserEntitiesByEmail(user.getEmail()) != null) {
            throw new EmailExistException("This email already exist. Chose other email address");
        }
    }

    public void registryNewUser(RegistrationUserDto user) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(user.getUsername());
        userEntity.setCreatedAt(LocalDate.now());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userEntity.setEmail(user.getEmail());

        userEntity = userRepository.save(userEntity);
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(userEntity, getAppUrl()));

    }


    public void createVerificationTokenForUser(UserEntity user, String token) {
        final VerificationToken myToken = new VerificationToken(token, user.getId());
        verificationTokenRepository.save(myToken);
    }


    private String getAppUrl() {
        return "http://" + appUrl + "/api/user/";
    }
}
