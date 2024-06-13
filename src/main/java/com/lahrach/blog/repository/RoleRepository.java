package com.lahrach.blog.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.lahrach.blog.model.Role;
import com.lahrach.blog.util.enums.RoleName;

public interface RoleRepository extends CrudRepository<Role, Integer> {
    Optional<Role> findByName(RoleName roleName);
}
