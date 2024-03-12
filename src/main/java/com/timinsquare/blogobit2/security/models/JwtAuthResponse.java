package com.timinsquare.blogobit2.security.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class JwtAuthResponse {
    private String jwt;
}
