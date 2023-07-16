package com.exam.controller;
import com.exam.enums.Status;
import com.exam.service.AuthService;
import com.exam.response.ExamPortalResponse;
import com.exam.request.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.exam.constants.ExamPortalConstant.AUTH_BASE_PATH;
import static com.exam.constants.ExamPortalConstant.LOGIN;

@RestController
@RequestMapping(AUTH_BASE_PATH)
public class AuthController {
	
	@Autowired
	private AuthService authService;
	
	@PostMapping(LOGIN)
	public ResponseEntity<ExamPortalResponse<?>> login(@RequestBody LoginRequest loginRequest){
		return new ResponseEntity<>(ExamPortalResponse.builder()
				.status(Status.SUCCESS)
				.message("User Logged in successfully")
				.body(authService.loginUser(loginRequest))
				.build(),
				HttpStatus.OK); 	
	}

}
