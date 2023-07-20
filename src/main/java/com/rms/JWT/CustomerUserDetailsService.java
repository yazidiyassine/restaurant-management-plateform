package com.rms.JWT;

import com.rms.dao.UserDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;


@Service
@Slf4j
public class CustomerUserDetailsService implements UserDetailsService {

    @Autowired
    UserDao userDoa;

    private com.rms.model.User userDetail;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Inside loadUserByUsername {} ", username);
        userDetail = userDoa.findByEmailId(username);
        if (!Objects.isNull(userDetail)){
            return new User(userDetail.getEmail(), userDetail.getPassword(), new ArrayList<>());
        }else{
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }

    public com.rms.model.User getUserDetail(){
        return userDetail;
    }
}

