package com.timinsquare.blogobit2.controllers;

import com.timinsquare.blogobit2.security.models.JwtAuthResponse;
import com.timinsquare.blogobit2.security.models.SignInRequest;
import com.timinsquare.blogobit2.security.models.SignUpRequest;
import com.timinsquare.blogobit2.services.AuthService;
import com.timinsquare.blogobit2.util.ErrorMessageBuilder;
import com.timinsquare.blogobit2.util.NotValidCredentials;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final ErrorMessageBuilder errorMessageBuilder;

    @Autowired
    public AuthController(AuthService authService, ErrorMessageBuilder errorMessageBuilder) {
        this.authService = authService;
        this.errorMessageBuilder = errorMessageBuilder;
    }

    @PostMapping("/sign-up")
    public JwtAuthResponse signUp(@RequestBody @Valid SignUpRequest request,
                                  BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = errorMessageBuilder.of(bindingResult);
            throw new NotValidCredentials(errorMessage);
        }

        return authService.signUp(request); // throws CredentialsAlreadyExist
    }

    @PostMapping("/sign-in")
    public JwtAuthResponse signIn(@RequestBody @Valid SignInRequest request) {
        try {
            return authService.signIn(request);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Incorrect credentials");
        }
    }

}
