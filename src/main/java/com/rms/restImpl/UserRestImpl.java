package com.rms.restImpl;

import com.rms.constants.RestoConstants;
import com.rms.rest.UserRest;
import com.rms.service.UserService;
import com.rms.utils.RestoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UserRestImpl implements UserRest {

    @Autowired
    private UserService userService;

    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
       try{
            return userService.signUp(requestMap);
       }catch(Exception ex){
           ex.printStackTrace();
       }
    return RestoUtils.getResponseEntity(RestoConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
