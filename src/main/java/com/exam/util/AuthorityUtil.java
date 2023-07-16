package com.exam.util;

import com.exam.enums.UserRole;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthorityUtil {

    public static Boolean isAdminRole(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getAuthorities().toString().contains(UserRole.ADMIN.getValue())){
            return true;
        }
        return false;
    }

    public static Boolean isSameUser(String userName){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(userName.equals(authentication.getPrincipal())){
            return true;
        }
        return false;
    }
}
