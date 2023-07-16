package com.exam.dto;

import com.exam.enums.Gender;
import com.exam.enums.UserStatus;
import com.exam.serializer.UserDTOSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties( value = {"password"}, allowSetters =  true )
@JsonSerialize(using = UserDTOSerializer.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long userId;
    private String name;
    private String userName;
    private String password;
    private String email;
    private String phoneNumber;
    private String userImage;
    private Gender gender;
    private Set<RoleDTO> roles = new HashSet<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserStatus status;
    private Boolean isUserVerified;
    private Boolean isAccountExpired;
    private Boolean isCredentialsExpired;
    private Boolean isAccountLocked;

}
