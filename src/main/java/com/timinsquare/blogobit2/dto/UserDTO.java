package com.timinsquare.blogobit2.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UserDTO {
    @NotEmpty(message = "Username must be filled in")
    private String username;
    @NotEmpty(message = "Password must be filled in")
    private String password;
    @NotEmpty(message = "Password must be confirm")
    private String confirmPassword;
}
