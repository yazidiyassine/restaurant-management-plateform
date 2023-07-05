package com.rms.dao;

import com.rms.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDoa extends JpaRepository<User, Integer> {
}
