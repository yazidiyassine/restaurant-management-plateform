package com.rms.serviceImpl;

import com.google.common.base.Strings;
import com.rms.JWT.CustomerUserDetailsService;
import com.rms.JWT.JwtFilter;
import com.rms.JWT.JwtUtil;
import com.rms.constants.RestoConstants;
import com.rms.dao.UserDao;
import com.rms.model.User;
import com.rms.service.UserService;
import com.rms.utils.EmailUtils;
import com.rms.utils.RestoUtils;
import com.rms.wrapper.UserWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDoa;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    CustomerUserDetailsService customerUserDetailsService;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    JwtFilter jwtFilter;

    @Autowired
    EmailUtils emailUtils;

    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        log.info("Inside SignUp {} ", requestMap);
        try {
            if (validateSignUpMap(requestMap)) {
                User user = userDoa.findByEmailId(requestMap.get("email"));
                if (Objects.isNull(user)) {
                    userDoa.save(getUserFromMap(requestMap));
                    return RestoUtils.getResponseEntity("Successfully registered!", HttpStatus.OK);
                } else {
                    return RestoUtils.getResponseEntity("Email already exists!", HttpStatus.BAD_REQUEST);
                }
            } else {
                return RestoUtils.getResponseEntity(RestoConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            log.error("Exception while signing up {}", e);
            e.printStackTrace();
        }
        return RestoUtils.getResponseEntity(RestoConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        log.info("Inside login");
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestMap.get("email"), requestMap.get("password"))
            );
            if (authentication.isAuthenticated()) {
                if (customerUserDetailsService.getUserDetail().getStatus().equalsIgnoreCase("true")) {
                    return new ResponseEntity<>("{\"token\":\"" + jwtUtil.generateToken(customerUserDetailsService.getUserDetail().getEmail(),
                            customerUserDetailsService.getUserDetail().getRole()) + "\"}", HttpStatus.OK);
                } else {
                    return new ResponseEntity<String>("{\"message\":\"" + "Please verify your email!" + "\"}", HttpStatus.UNAUTHORIZED);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<String>("{\"message\":\"" + "Invalid Credentials!" + "\"}", HttpStatus.UNAUTHORIZED);
    }

    @Override
    public ResponseEntity<List<UserWrapper>> getAllUsers() {
        try {
            if (jwtFilter.isAdmin()) {
                return new ResponseEntity<>(userDoa.getAllUsers(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> update(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isAdmin()) {
                Optional<User> optionalUser = userDoa.findById(Integer.parseInt(requestMap.get("id")));
                if (!optionalUser.isEmpty()) {
                    userDoa.updateStatus(requestMap.get("status"), Integer.parseInt(requestMap.get("id")));
                    sendMailToAllAdmin(requestMap.get("status"), optionalUser.get().getEmail(), userDoa.getAllAdmins());
                    return RestoUtils.getResponseEntity("User status updated successfully", HttpStatus.OK);
                } else {
                    return RestoUtils.getResponseEntity("User id does not exist", HttpStatus.BAD_REQUEST);
                }
            } else {
                return RestoUtils.getResponseEntity(RestoConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RestoUtils.getResponseEntity(RestoConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void sendMailToAllAdmin(String status, String user, List<String> allAdmins) {

        allAdmins.remove(jwtFilter.getCurrentUser());
        if (status != null && status.equalsIgnoreCase("true")) {
            emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(), "Account Approved", "USER:- " + user + " has been approved by Admin:- " + jwtFilter.getCurrentUser() + ".", allAdmins);
        } else {
            emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(), "Account Rejected", "USER:- " + user + " has been rejected by Admin:- " + jwtFilter.getCurrentUser() + ".", allAdmins);
        }
    }

    private boolean validateSignUpMap(Map<String, String> reqMap) {
        return reqMap.containsKey("name") && reqMap.containsKey("contactNumber")
                && reqMap.containsKey("email") && reqMap.containsKey("password");
    }

    private User getUserFromMap(Map<String, String> reqMap) {
        User user = new User();
        user.setName(reqMap.get("name"));
        user.setContactNumber(reqMap.get("contactNumber"));
        user.setEmail(reqMap.get("email"));
        user.setPassword(reqMap.get("password"));
        user.setStatus("false");
        user.setRole("USER");
        return user;
    }

    @Override
    public ResponseEntity<String> checkToken() {
        return RestoUtils.getResponseEntity("Token is valid", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
        try {
            User user = userDoa.findByEmailId(jwtFilter.getCurrentUser());
            if (user != null){
                if (user.getPassword().equals(requestMap.get("oldPassword"))) {
                    user.setPassword(requestMap.get("newPassword"));
                    userDoa.save(user);
                    return RestoUtils.getResponseEntity("Password changed successfully", HttpStatus.OK);
                }
                return RestoUtils.getResponseEntity("Old password is incorrect", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RestoUtils.getResponseEntity(RestoConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> forgotPassword(Map<String, String> requestMap) {
        try{
            User user = userDoa.findByEmailId(requestMap.get("email"));
            if (user != null && !Strings.isNullOrEmpty(user.getEmail())){
                emailUtils.forgotMail(user.getEmail(), "Credentials by RMS", user.getPassword());
                return RestoUtils.getResponseEntity("Check your mail for Credentials", HttpStatus.OK);
            }
            return RestoUtils.getResponseEntity("Check your mail for Credentials", HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            e.printStackTrace();
        }
        return RestoUtils.getResponseEntity(RestoConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
