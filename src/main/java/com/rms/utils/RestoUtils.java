package com.rms.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class RestoUtils {

    private RestoUtils(){

    }

    public static ResponseEntity<String> getResponseEntity(String responseMessage, HttpStatus httpStatus){
        return new ResponseEntity<String>("{\"message\":\""+responseMessage+"\"}",httpStatus);
    }

}
