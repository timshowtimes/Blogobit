package com.timinsquare.blogobit2.util;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleJwtException(Exception e) {
        ProblemDetail errorDetail = null;
        if (e instanceof NotValidCredentials) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
            errorDetail.setProperty("access-denied-reason", "Validation Failure");
        }
        if (e instanceof CredentialsAlreadyExist) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
            errorDetail.setProperty("access-denied-reason", "Existing credentials");
        }
        if (e instanceof BadCredentialsException) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(401),
                    e.getMessage());
            errorDetail.setProperty("access-denied-reason", "Authentication Failure");
        }
        if (e instanceof AccessDeniedException) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403),
                    e.getMessage());
            errorDetail.setProperty("access-denied-reason", "Not authorized");
        }
        if (e instanceof SignatureException) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403),
                    e.getMessage());
            errorDetail.setProperty("access-denied-reason", "Jwt Signature not valid");
        }
        if (e instanceof ExpiredJwtException) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403),
                    e.getMessage());
            errorDetail.setProperty("access-denied-reason", "Jwt token already expired");
        }
        return errorDetail;
    }

}
