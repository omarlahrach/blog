package com.lahrach.blog.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import com.lahrach.blog.model.User;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
