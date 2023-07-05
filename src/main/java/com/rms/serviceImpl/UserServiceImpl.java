package com.rms.serviceImpl;

import com.rms.constants.RestoConstants;
import com.rms.dao.UserDoa;
import com.rms.service.UserService;
import com.rms.utils.RestoUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

//    private UserDoa userDoa;
    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        log.info("Inside SignUp {} ", requestMap);
        if (validateSignUpMap(requestMap)) {

        }else{
            return RestoUtils.getResponseEntity(RestoConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
        }
    }

    private boolean validateSignUpMap(Map<String, String> reqMap){
        return reqMap.containsKey("name") && reqMap.containsKey("contactNumber")
                && reqMap.containsKey("email") && reqMap.containsKey("password");
    }
}
