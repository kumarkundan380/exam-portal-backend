package com.exam.service;

import com.exam.dto.UserDTO;
import com.exam.enums.UserRole;
import com.exam.request.PasswordChangeRequest;
import com.exam.response.PageableResponse;

import java.util.List;

public interface UserService {

    UserDTO addUser(UserDTO userDTO);
    PageableResponse<?> getAllUsers(Integer pageNumber, Integer pageSize);
    void deleteUser(Long userId);
    UserDTO getUserByUsername(String userName);
    UserDTO getUserById(Long userId);
    String verifyUser(String token);
    String changePassword(PasswordChangeRequest passwordChangeRequest, Long userId);
    Boolean isDeletedUser(String userName);
    Boolean isUserExist(String userName);
    UserDTO updateUser(Long userId, UserDTO userDTO);
    UserDTO updateUserRole(List<UserRole> userRoles, Long userId);
}
