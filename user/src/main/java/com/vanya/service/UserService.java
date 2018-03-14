package com.vanya.service;

import com.vanya.dao.ChangePasswordTokenRepository;
import com.vanya.dao.UserRepository;
import com.vanya.dao.VerificationTokenRepository;
import com.vanya.dto.*;
import com.vanya.events.OnChangePasswordEvent;
import com.vanya.events.OnRegistrationCompleteEvent;
import com.vanya.exception.EmailExistException;
import com.vanya.exception.UserNameExistException;
import com.vanya.model.UserEntity;
import com.vanya.model.token.ChangePasswordToken;
import com.vanya.model.token.VerificationToken;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

@Service
@Slf4j
public class UserService {

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${app.url}")
    private String appUrl;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private ChangePasswordTokenRepository passwordTokenRepository;

//    @PostConstruct
//    public void afterPropertiesSet() {
//        for(int i=0;i<100;++i) {
//            UserEntity userEntity = new UserEntity();
//            userEntity.setUsername("VVVV"+i);
//            userEntity.setPassword(passwordEncoder.encode("qweqwe"+i));
//            userEntity.setEmail("gladush97@gmail.com"+i);
//            userEntity.setCreatedAt(new Date(System.currentTimeMillis()));
//            userEntity.setFirstName("firstName"+i);
//            userEntity.setSurname("Surname"+i);
//            userEntity.setAuthorities(new SimpleGrantedAuthority("ROLE_USER"));
//            userEntity.setPathToPhoto("user-1.jpg");
//            userEntity.setEnabled(true);
//            userRepository.save(userEntity);
//        }
//    }

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

    public void createChangePasswordTokenForUser(UserEntity user, String token) {
        final ChangePasswordToken passwordToken = new ChangePasswordToken(token, user);
        passwordTokenRepository.save(passwordToken);
    }

    public void registryNewUser(RegistrationUserDto user) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(user.getUsername());
        userEntity.setCreatedAt(new Date(System.currentTimeMillis()));
        userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
        userEntity.setEmail(user.getEmail());
        userEntity.setFirstName(user.getFirstName());
        userEntity.setSurname(user.getSurname());

        userEntity = userRepository.save(userEntity);
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(userEntity, getAppUrl()));

    }


    public void createVerificationTokenForUser(UserEntity user, String token) {
        final VerificationToken myToken = new VerificationToken(token, user.getId());
        verificationTokenRepository.save(myToken);
    }

    public boolean isEmailExist(final String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean isConfirmedEmail(final String email) {
        final Boolean userEnabled = userRepository.getUserEnabledByEmail(email);
        if (userEnabled == null) {
            throw new UserNameExistException("This email doesn't exist");
        }
        return userEnabled;
    }

    public void resendRegistrationToken(final String email) {
        UserEntity userEntity = userRepository.findUserEntitiesByEmail(email);
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(userEntity, getAppUrl()));

    }

    public String confirmUserRegistration(String token) {
        final VerificationToken verificationToken = verificationTokenRepository.findByToken(token);

        if (verificationToken == null) {
            return VerificationToken.TOKEN_INVALID;
        } else if (verificationToken.getExpiryDate().getTime() - System.currentTimeMillis() <= 0) {
            verificationTokenRepository.delete(verificationToken);
            return VerificationToken.TOKEN_EXPIRED;
        }

        final Long userId = verificationToken.getUserId();

        userRepository.enableUser(userId);
        verificationTokenRepository.delete(verificationToken);

        log.info("Confirm user registration. userId: {}", userId);

        return VerificationToken.TOKEN_VALID;
    }

    public void sendTokenForChangePassword(String email) {
        UserEntity userEntity = userRepository.findUserEntitiesByEmail(email);
        eventPublisher.publishEvent(new OnChangePasswordEvent(userEntity, getAppUrl()));
    }

    private String getAppUrl() {
        return "http://" + appUrl;
    }


    public boolean isValidChangePasswordToken(final String token) {
        final ChangePasswordToken changeToken = passwordTokenRepository.findOneByToken(token);

        return changeToken != null && changeToken.getExpiryDate().getTime() - System.currentTimeMillis() > 0;
    }

    public void changeUserPasswordViaToken(final ChangePasswordDTO passwordDTO) {
        final ChangePasswordToken changeToken = passwordTokenRepository.findOneByToken(passwordDTO.getToken());
        userRepository.setNewPassword(passwordEncoder.encode(passwordDTO.getPassword()), changeToken.getUserId());
        passwordTokenRepository.delete(changeToken);
    }

    public UserDto getCurrentUser(String currentUserName) {

        return this.getUserByName(currentUserName).get();

    }

    public void changeUserPassword(UpdatePasswordDTO newPassword, long userId) {
        userRepository.setNewPassword(passwordEncoder.encode(newPassword.getPassword()), userId);
    }

    public void setNewPhoto(long userId, String pathToPhoto) {
        userRepository.setPassToPhoto(userId, pathToPhoto);
    }

    public void changeUser(long userId, UpdateUserDto newUser) {
        userRepository.updateUser(userId,
                newUser.getEmail(),
                newUser.getPhoneNumber(),
                newUser.getFirstName(),
                newUser.getSurname(),
                newUser.getBirthDate());
    }

    public Page<UserDto> findAllPageable(PageRequest pageable) {
        return
                userRepository.findAll(pageable).map(x -> mapper.map(x, UserDto.class));
    }
}
