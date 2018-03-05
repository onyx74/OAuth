package com.vanya.dao;

import com.vanya.model.token.ChangePasswordToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChangePasswordTokenRepository extends CrudRepository<ChangePasswordToken, Long> {
    ChangePasswordToken findOneByToken(String token);
}
