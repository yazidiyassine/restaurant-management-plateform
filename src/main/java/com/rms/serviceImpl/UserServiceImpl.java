package com.rms.serviceImpl;

import com.rms.constants.RestoConstants;
import com.rms.dao.UserDoa;
import com.rms.model.User;
import com.rms.service.UserService;
import com.rms.utils.RestoUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDoa userDoa;
    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        log.info("Inside SignUp {} ", requestMap);
        try{
            if (validateSignUpMap(requestMap)) {
                User user = userDoa.findByEmailId(requestMap.get("email"));
                if (Objects.isNull(user)) {
                    userDoa.save(getUserFromMap(requestMap));
                    return RestoUtils.getResponseEntity("Successfully registered!", HttpStatus.OK);
                }else{
                    return RestoUtils.getResponseEntity("Email already exists!", HttpStatus.BAD_REQUEST);
                }
            } else {
                return RestoUtils.getResponseEntity(RestoConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            log.error("Exception while signing up {}", e);
            e.printStackTrace();
        }
        return RestoUtils.getResponseEntity(RestoConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
    }

    private boolean validateSignUpMap(Map<String, String> reqMap){
        return reqMap.containsKey("name") && reqMap.containsKey("contactNumber")
                && reqMap.containsKey("email") && reqMap.containsKey("password");
    }

    private User getUserFromMap(Map<String, String> reqMap){
        User user = new User();
        user.setName(reqMap.get("name"));
        user.setContactNumber(reqMap.get("contactNumber"));
        user.setEmail(reqMap.get("email"));
        user.setPassword(reqMap.get("password"));
        user.setStatus("false");
        user.setRole("USER");
        return user;
    }
}
