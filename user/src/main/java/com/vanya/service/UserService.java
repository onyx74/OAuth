package com.vanya.service;

import com.vanya.dao.ChangePasswordTokenRepository;
import com.vanya.dao.UserRepository;
import com.vanya.dao.VerificationTokenRepository;
import com.vanya.dto.ChangePasswordDTO;
import com.vanya.dto.RegistrationUserDto;
import com.vanya.dto.UserDto;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

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
        userEntity.setCreatedAt(LocalDate.now());
        userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
        userEntity.setEmail(user.getEmail());

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

    public void changeUserPassword(final ChangePasswordDTO passwordDTO) {
        final ChangePasswordToken changeToken = passwordTokenRepository.findOneByToken(passwordDTO.getToken());
        userRepository.setNewPassword(passwordEncoder.encode(passwordDTO.getPassword()), changeToken.getUserId());
        passwordTokenRepository.delete(changeToken);
    }
}
