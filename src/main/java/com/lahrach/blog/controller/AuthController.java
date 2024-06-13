package com.lahrach.blog.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lahrach.blog.dto.LoginRequest;
import com.lahrach.blog.dto.RegisterRequest;
import com.lahrach.blog.dto.AuthResponse;
import com.lahrach.blog.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest registerRequest,
            HttpServletResponse registerResponse) {
        AuthResponse authResponse = authService.register(registerRequest, registerResponse);
        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest,
            HttpServletResponse loginResponse) {
        AuthResponse authResponse = authService.login(loginRequest, loginResponse);
        return ResponseEntity.ok(authResponse);
    }

    @GetMapping("/refresh-token")
    public ResponseEntity<String> refreshToken(@CookieValue(name = "refresh-token") String refreshToken) {
        System.out.println(refreshToken);
        String newAccessToken = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(newAccessToken);
    }
}
