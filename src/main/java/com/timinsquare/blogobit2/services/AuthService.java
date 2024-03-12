package com.timinsquare.blogobit2.services;

import com.timinsquare.blogobit2.enums.Role;
import com.timinsquare.blogobit2.models.BUser;
import com.timinsquare.blogobit2.security.models.SignInRequest;
import com.timinsquare.blogobit2.security.jwt.JWTUtil;
import com.timinsquare.blogobit2.security.models.JwtAuthResponse;
import com.timinsquare.blogobit2.security.models.SignUpRequest;
import com.timinsquare.blogobit2.util.CredentialsAlreadyExist;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final BUserDetailsService userDetailsService;
    private final JWTUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public JwtAuthResponse signUp(@RequestBody SignUpRequest request) throws CredentialsAlreadyExist {
        BUser user = BUser.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_USER)
                .build();

        userDetailsService.create(user); // throws CredentialsAlreadyExist
        String jwt = jwtUtil.generateToken(user);
        return new JwtAuthResponse(jwt);
    }

    public JwtAuthResponse signIn(@RequestBody SignInRequest request) throws BadCredentialsException {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ));

        UserDetails user = userDetailsService.loadUserByUsername(request.getUsername());
        String jwt = jwtUtil.generateToken(user);
        return new JwtAuthResponse(jwt);
    }
}
