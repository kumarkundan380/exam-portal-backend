package com.exam.response;

import com.exam.dto.UserDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class LoginResponse {

    private String token;
    private UserDTO user;
}
