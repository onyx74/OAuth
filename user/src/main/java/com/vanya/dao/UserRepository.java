package com.vanya.dao;

import com.vanya.model.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

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

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE UserEntity u SET u.email = ?2, u.phoneNumber=?3, u.firstName=?4, u.surname=?5,u.birthDate=?6" +
            " WHERE u.id = ?1")
    int updateUser(long userId, String email, String phoneNumber, String firstName, String surname, Date birthDate);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE UserEntity  u SET u.password= ?1 WHERE u.id= ?2")
    int setNewPassword(String password, long userId);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE UserEntity  u SET u.pathToPhoto= ?2 WHERE u.id= ?1")
    int setPassToPhoto(long userId, String pathToPhoto);

    boolean findByEmailEqualsAndEnabledTrue(String email);

    Page<UserEntity> findAllByUsernameLike(String userName, Pageable pageable);

    List<UserEntity> findAllByIdIn(List<Long> ids);

    List<UserEntity> findAllByUsernameLike(String userName);
}
