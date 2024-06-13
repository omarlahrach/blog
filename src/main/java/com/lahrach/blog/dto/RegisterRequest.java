package com.lahrach.blog.dto;

import lombok.Builder;

@Builder
public record RegisterRequest(String firstName, String lastName, String email, String password) {
}
