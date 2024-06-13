package com.lahrach.blog.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletResponse;

@Service
public class CookieService {

    public void saveRefreshToken(String refreshToken, HttpServletResponse response) {
        ResponseCookie resCookie = ResponseCookie.from("refresh-token", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(1800)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, resCookie.toString());
    }
    
}
