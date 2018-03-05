package com.vanya.dao;

import com.vanya.model.UserEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long> {

    UserEntity findUserEntitiesByUsername(String userName);

    UserEntity findUserEntitiesByEmail(String email);

    @Query("select u.id from UserEntity u where u.username = ?1")
    long findUserIdByUsername(String userName);

    @Query("select  u.enabled from UserEntity  u where u.email= ?1")
    Boolean getUserEnabledByEmail(String email);

    boolean existsByEmail(String email);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE UserEntity u SET u.enabled = false WHERE u.email = :email")
    int disableUser(@Param("email") String email);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE UserEntity u SET u.enabled = true WHERE u.id = :userId")
    int enableUser(@Param("userId") Long userId);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE UserEntity  u SET u.password= ?1 WHERE u.username= ?2")
    int setNewPassword(String password, String userName);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE UserEntity  u SET u.pathToPhoto= ?1 WHERE u.username= ?2")
    int setPassToPhoto(String pathToPhoto, String userName);

    boolean findByEmailEqualsAndEnabledTrue(String email);
}
