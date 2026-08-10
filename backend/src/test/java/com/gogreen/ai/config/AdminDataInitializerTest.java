package com.gogreen.ai.config;

import com.gogreen.ai.entity.Role;
import com.gogreen.ai.entity.User;
import com.gogreen.ai.entity.enums.UserRole;
import com.gogreen.ai.repository.RoleRepository;
import com.gogreen.ai.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Integration test verifying the AdminDataInitializer startup behavior.
 * Uses the default in-memory H2 database, so the full Spring context
 * (including the CommandLineRunner) is loaded on startup.
 */
@SpringBootTest
@Transactional
class AdminDataInitializerTest {

    @Autowired
    private AdminDataInitializer adminDataInitializer;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void applicationStartup_createsDefaultAdmin() {
        // 1. Database connection works & users table exists -> repository queries succeed below.
        // 2. The default ADMIN user is inserted on startup (CommandLineRunner ran).
        Optional<User> adminOptional = userRepository.findByUsername("ashakumar");
        assertTrue(adminOptional.isPresent(), "Default admin should exist after startup");

        User admin = adminOptional.get();
        assertEquals("admin@gogreen.ai", admin.getEmail());
        assertEquals("Asha Kumar", admin.getName());
        assertTrue(admin.isEnabled());
        assertEquals(com.gogreen.ai.entity.enums.UserApprovalStatus.APPROVED, admin.getApprovalStatus());

        // Admin has the ROLE_ADMIN role.
        assertTrue(admin.getRoles().stream()
                .anyMatch(r -> r.getName() == UserRole.ROLE_ADMIN),
                "Admin must have ROLE_ADMIN");

        // 3. Password is BCrypt encoded (must not equal plain text, must match via BCrypt).
        String storedPassword = admin.getPassword();
        assertNotNull(storedPassword);
        assertTrue(storedPassword.startsWith("$2"), "Password should be a BCrypt hash");
        assertTrue(passwordEncoder.matches("Password123!", storedPassword),
                "Encoded password must match the plain-text password");
    }

    @Test
    void runningInitializerAgain_doesNotCreateDuplicateAdmin() {
        // 4. Run the initializer manually a second time -> idempotent,
        //    no duplicate admin should be created.
        adminDataInitializer.run();

        long count = userRepository.findByUsername("ashakumar")
                .map(u -> 1L)
                .orElse(0L);
        assertEquals(1L, count, "Restarting/running initializer again must not create a duplicate admin");

        // Also verify only one admin role linkage exists for this username.
        Optional<User> adminOptional = userRepository.findByUsername("ashakumar");
        assertTrue(adminOptional.isPresent());
        assertEquals(1, adminOptional.get().getRoles().size());
    }

    @Test
    void roleEnumReused_existingAdminRoleUsed() {
        // The initializer must reuse the existing UserRole.ROLE_ADMIN enum value.
        assertTrue(roleRepository.existsByName(UserRole.ROLE_ADMIN), "ROLE_ADMIN role should exist");
    }
}
