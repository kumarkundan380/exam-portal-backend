package com.exam.service.impl;

import com.exam.request.LoginRequest;
import com.exam.response.LoginResponse;
import com.exam.security.JwtUtil;
import com.exam.service.AuthService;
import com.exam.service.UserService;
import com.exam.exception.ExamPortalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public LoginResponse loginUser(LoginRequest loginRequest) {
        log.info("loginUser method invoking");
        loginRequest = validateLoginRequest(loginRequest);
        authenticate(loginRequest);
        String token = jwtUtil.generateToken(loginRequest.getUserName());
        return LoginResponse.builder()
                .token(token)
                .user(userService.getUserByUsername(loginRequest.getUserName()))
                .build();

    }

    private void authenticate(LoginRequest loginRequest) {
        log.info("authenticate method invoking");
        try {
             authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUserName().trim().toLowerCase(),
                            loginRequest.getPassword().trim()));
        } catch(BadCredentialsException e) {
            throw new ExamPortalException("Enter valid Username and Password");
        }
    }

    private LoginRequest validateLoginRequest(LoginRequest loginRequest){
        log.info("validateLoginRequest method invoking");
        if(!StringUtils.hasText(loginRequest.getUserName())){
            throw new ExamPortalException("Username cannot be empty");
        }
        if(!StringUtils.hasText(loginRequest.getPassword())){
            throw new ExamPortalException("Password cannot be empty");
        }
        if(userService.isUserExist(loginRequest.getUserName()) && userService.isDeletedUser(loginRequest.getUserName())){
            throw new ExamPortalException("Either User does not exist or User is deleted!!");
        }
        loginRequest.setUserName(loginRequest.getUserName().trim().toLowerCase());
        loginRequest.setPassword(loginRequest.getPassword().trim());
        return loginRequest;
    }
}
