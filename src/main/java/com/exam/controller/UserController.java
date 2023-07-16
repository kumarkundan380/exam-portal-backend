package com.exam.controller;

import com.exam.dto.UserDTO;
import com.exam.enums.Status;
import com.exam.enums.UserRole;
import com.exam.response.ExamPortalResponse;
import com.exam.service.UserService;
import com.exam.request.PasswordChangeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.exam.constants.ExamPortalConstant.*;

@RestController
@RequestMapping(USER_BASE_PATH)
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<ExamPortalResponse<?>> addUser(@RequestBody UserDTO userDTO){
        return new ResponseEntity<>(ExamPortalResponse.builder()
                .status(Status.SUCCESS)
                .message("Welcome to Exam Portal, please verify your email address")
                .body(userService.addUser(userDTO))
                .build(),
                HttpStatus.CREATED);
    }

    @PutMapping("/{"+ USER_PARAMETER + "}")
    public ResponseEntity<ExamPortalResponse<?>> updateUser(@PathVariable Long userId,
                                                            @RequestBody UserDTO userDTO){
        return new ResponseEntity<>(ExamPortalResponse.builder()
                .status(Status.SUCCESS)
                .message("User updated Successfully")
                .body(userService.updateUser(userId,userDTO))
                .build(),
                HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<ExamPortalResponse<?>> getAllUsers(@RequestParam(value = "pageNumber", defaultValue = PAGE_NUMBER) Integer pageNumber,
                                                             @RequestParam(value = "pageSize", defaultValue = PAGE_SIZE) Integer pageSize){
        return new ResponseEntity<>(ExamPortalResponse.builder()
                .status(Status.SUCCESS)
                .message("Fetch User Successfully")
                .body(userService.getAllUsers(pageNumber,pageSize))
                .build(),
                HttpStatus.OK);
    }

    @GetMapping("/{"+ USER_PARAMETER + "}")
    public ResponseEntity<ExamPortalResponse<?>> getOneUser(@PathVariable Long userId){
        return new ResponseEntity<>(ExamPortalResponse.builder()
                .status(Status.SUCCESS)
                .message("Fetch User Successfully")
                .body(userService.getUserById(userId))
                .build(),
                HttpStatus.OK);
    }
    @DeleteMapping("/{"+ USER_PARAMETER +"}")
    public ResponseEntity<ExamPortalResponse<?>> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return new ResponseEntity<>(ExamPortalResponse.builder()
                .status(Status.SUCCESS)
                .message("User Deleted Successfully")
                .build(),
                HttpStatus.OK);
    }

    @GetMapping(VERIFY_USER + "/{" + TOKEN_PARAMETER + "}")
    public ResponseEntity<ExamPortalResponse<?>> verifyUser(@PathVariable String token){
        return new ResponseEntity<>(ExamPortalResponse.builder()
                .status(Status.SUCCESS)
                .message(userService.verifyUser(token))
                .build(),
                HttpStatus.OK);
    }

    @PutMapping("/{" + USER_PARAMETER + "}" + CHANGE_PASSWORD)
    public ResponseEntity<ExamPortalResponse<?>> changePassword(@RequestBody PasswordChangeRequest passwordChangeRequest,
                                                             @PathVariable Long userId){
        return new ResponseEntity<>(ExamPortalResponse.builder()
                .status(Status.SUCCESS)
                .message("Password changed successfully")
                .body(userService.changePassword(passwordChangeRequest,userId))
                .build(),
                HttpStatus.OK);
    }

    @PutMapping("/{" + USER_PARAMETER + "}" + ROLES)
    public ResponseEntity<ExamPortalResponse<?>> updateUserRoles(@RequestBody List<UserRole> userRoles,
                                                                @PathVariable Long userId){
        return new ResponseEntity<>(ExamPortalResponse.builder()
                .status(Status.SUCCESS)
                .message("Roles update successfully")
                .body(userService.updateUserRole(userRoles,userId))
                .build(),
                HttpStatus.OK);
    }


}
