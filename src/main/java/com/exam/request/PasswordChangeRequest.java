package com.exam.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PasswordChangeRequest {

    @NotBlank(message = "Old Password must not be null or empty")
    private String oldPassword;

    @NotBlank(message = "New Password must not be null or empty")
    private String newPassword;
}
