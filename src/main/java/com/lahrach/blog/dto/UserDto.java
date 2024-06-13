package com.lahrach.blog.dto;

import lombok.Builder;

@Builder
public record UserDto(String firstName, String lastName, String email) {
}
