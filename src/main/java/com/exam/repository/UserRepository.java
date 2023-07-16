package com.exam.repository;

import com.exam.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserName(String userName);
    Optional<User> findByEmail(String email);
    Optional<User> findByPhoneNumber(String phoneNumber);
    @Modifying
    @Query("update User u set u.userStatus='DELETED' where u.userId = ?1")
    void deleteUser(Long userId);
    @Query(value = "SELECT u FROM User u WHERE u.userStatus = 'ACTIVE' AND u.isVerified = '1' ")
    Page<User> findAllUser(Pageable pageable);
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.userName = ?1 AND u.userStatus = 'DELETED' ")
    Boolean isDeletedUser(String userName);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.userName = ?1 ")
    Boolean isUserExist(String userName);

}
