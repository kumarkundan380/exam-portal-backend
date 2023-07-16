package com.exam.service;

import com.exam.request.LoginRequest;
import com.exam.response.LoginResponse;

public interface AuthService {

    LoginResponse loginUser(LoginRequest loginRequest);
}
