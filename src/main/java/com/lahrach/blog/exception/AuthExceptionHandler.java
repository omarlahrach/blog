package com.lahrach.blog.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;

@RestControllerAdvice
public class AuthExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleException(Exception exception, WebRequest request) {
        if (exception instanceof BadCredentialsException) {
            return createProblemDetail(HttpStatus.UNAUTHORIZED, "The username or password is incorrect", exception);
        } else if (exception instanceof AccountStatusException) {
            return createProblemDetail(HttpStatus.FORBIDDEN, "The account is locked", exception);
        } else if (exception instanceof AccessDeniedException) {
            return createProblemDetail(HttpStatus.FORBIDDEN, "You are not authorized to access this resource",
                    exception);
        } else if (exception instanceof SignatureException) {
            return createProblemDetail(HttpStatus.FORBIDDEN, "The JWT signature is invalid", exception);
        } else if (exception instanceof ExpiredJwtException) {
            return createProblemDetail(HttpStatus.UNAUTHORIZED, "The JWT token has expired", exception);
        } else {
            return createProblemDetail(HttpStatus.INTERNAL_SERVER_ERROR, "An unknown error has occurred", exception);
        }
    }

    private ProblemDetail createProblemDetail(HttpStatus status, String message, Exception exception) {
        ProblemDetail errorDetail = ProblemDetail.forStatusAndDetail(status, exception.getMessage());
        errorDetail.setProperty("exception", exception.getClass().getSimpleName());
        errorDetail.setProperty("description", message);
        return errorDetail;
    }
}
