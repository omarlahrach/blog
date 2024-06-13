package com.lahrach.blog.mapper;

import org.springframework.stereotype.Service;

import com.lahrach.blog.dto.UserDto;
import com.lahrach.blog.model.User;

@Service
public class UserMapper implements Mapper<User, UserDto> {

    @Override
    public User toEntity(UserDto dto) {
        return User.builder()
                .firstName(dto.firstName())
                .lastName(dto.lastName())
                .email(dto.email())
                .build();
    }

    @Override
    public UserDto toDto(User entity) {
        return UserDto.builder()
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .email(entity.getEmail())
                .build();
    }
}
