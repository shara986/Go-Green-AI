package com.gogreen.ai.config;

import com.gogreen.ai.entity.Role;
import com.gogreen.ai.entity.User;
import com.gogreen.ai.entity.enums.UserApprovalStatus;
import com.gogreen.ai.entity.enums.UserRole;
import com.gogreen.ai.repository.RoleRepository;
import com.gogreen.ai.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

/**
 * Startup initializer that ensures a default ADMIN user exists.
 *
 * <p>It runs once on application startup and is idempotent: if an ADMIN user
 * already exists (matched by username or email), it takes no action. Otherwise
 * it creates the ADMIN role (reusing the existing {@link UserRole#ROLE_ADMIN}
 * enum) and a User assigned that role, with the password encoded via the
 * injected Spring Security {@link PasswordEncoder} (BCrypt).</p>
 */
@Component
public class AdminDataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(AdminDataInitializer.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.username}")
    private String adminUsername;

    @Value("${app.admin.email}")
    private String adminEmail;

    @Value("${app.admin.password}")
    private String adminPassword;

    @Value("${app.admin.name}")
    private String adminName;

    @Value("${app.admin.phone}")
    private String adminPhone;

    public AdminDataInitializer(UserRepository userRepository,
                                RoleRepository roleRepository,
                                PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        boolean adminExists = userRepository.existsByUsername(adminUsername)
                || userRepository.existsByEmail(adminEmail);

        if (adminExists) {
            log.info("Admin already exists; skipping initialization");
            return;
        }

        Role adminRole = roleRepository.findByName(UserRole.ROLE_ADMIN)
                .orElseGet(() -> roleRepository.save(new Role(UserRole.ROLE_ADMIN)));

        Set<Role> roles = new HashSet<>();
        roles.add(adminRole);

        User admin = new User();
        admin.setName(adminName);
        admin.setUsername(adminUsername);
        admin.setEmail(adminEmail);
        admin.setPassword(passwordEncoder.encode(adminPassword));
        admin.setPhoneNumber(adminPhone);
        admin.setEnabled(true);
        admin.setApprovalStatus(UserApprovalStatus.APPROVED);
        admin.setDeleted(false);
        admin.setRoles(roles);

        userRepository.save(admin);

        log.info("Default admin created successfully");
    }
}
