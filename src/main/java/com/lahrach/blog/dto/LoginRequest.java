package com.lahrach.blog.dto;

import lombok.Builder;

@Builder
public record LoginRequest(String email, String password) {
}
