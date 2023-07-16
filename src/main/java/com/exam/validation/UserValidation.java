package com.exam.validation;

import com.exam.dto.UserDTO;
import com.exam.exception.ExamPortalException;
import com.exam.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

@Slf4j
public class UserValidation {

    public static UserDTO validateUser(UserDTO user){
        log.info("validateUser method invoking");
        if(!StringUtils.hasText(user.getName())){
            log.error("Name is empty");
            throw new ExamPortalException("Name cannot be empty");
        } else if (!StringUtils.hasText(user.getUserName())) {
            log.error("Username is empty");
            throw new ExamPortalException("Username cannot be empty");
        } else if (!StringUtils.hasText(user.getPhoneNumber())) {
            log.error("Phone Number is empty");
            throw new ExamPortalException("Phone Number cannot be empty");
        } else if (!StringUtils.hasText(user.getEmail())) {
            log.error("Email is empty");
            throw new ExamPortalException("Email cannot be empty");
        } else if(user.getGender()==null){
            log.error("Gender is empty");
            throw new ExamPortalException("Gender cannot be empty");
        }
        log.info("Removing extra spaces if any mandatory fields contains");
        user.setUserName(user.getUserName().trim().toLowerCase());
        user.setEmail(user.getEmail().trim().toLowerCase());
        user.setName(user.getName().trim());
        user.setPhoneNumber(user.getPhoneNumber().trim());
        log.info("validateUser method called");
        return user;
    }

    public static Boolean matchPhoneNumberField(String oldPhoneNumber, String newPhoneNumber){
        if(oldPhoneNumber.equals(newPhoneNumber)){
            return true;
        }
        return false;
    }

    public static Boolean matchEmailField(User user, UserDTO userDTO){
        if(user.getEmail().equalsIgnoreCase(userDTO.getEmail())){
            return true;
        }
        return false;
    }
}
