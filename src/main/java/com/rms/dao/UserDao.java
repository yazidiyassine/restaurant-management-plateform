package com.rms.dao;

import com.rms.model.User;
import com.rms.wrapper.UserWrapper;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao extends JpaRepository<User, Integer> {

    User findByEmailId(@Param("email") String email);

    List<UserWrapper> getAllUsers();

    @Transactional
    @Modifying
    Integer updateStatus(@Param("status") String status, @Param("id") Integer id);

    List<String> getAllAdmins();
}
