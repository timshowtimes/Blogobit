package com.timinsquare.blogobit2.security.models;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class SignInRequest {
    @NotEmpty(message = "Username should not be empty")
    private String username;
    @NotEmpty(message = "Password should not be empty")
    private String password;
}
