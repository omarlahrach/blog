package com.lahrach.blog.dto;

import java.util.Set;

import com.lahrach.blog.util.enums.RoleName;

import lombok.Builder;

@Builder
public record AuthResponse(String accessToken, Long expiresIn, String firstName, String lastName, String email,
        Set<RoleName> roles) {
}
