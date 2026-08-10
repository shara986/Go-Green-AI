package com.gogreen.ai.repository;

import com.gogreen.ai.entity.Role;
import com.gogreen.ai.entity.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {

    Optional<Role> findByName(UserRole name);

    boolean existsByName(UserRole name);
}
