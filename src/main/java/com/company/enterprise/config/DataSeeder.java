package com.company.enterprise.config;

import com.company.enterprise.entity.RoleName;
import com.company.enterprise.entity.User;
import com.company.enterprise.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Seeds a default ADMIN user on first startup so the API is usable immediately
 * without a manual registration step. Username: admin / Password: admin123
 */
@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (!userRepository.existsByUsername("admin")) {
            User admin = User.builder()
                    .username("admin")
                    .email("admin@enterprise.com")
                    .password(passwordEncoder.encode("admin123"))
                    .role(RoleName.ADMIN)
                    .enabled(true)
                    .build();
            userRepository.save(admin);
            System.out.println(">>> Default admin user created -> username: admin | password: admin123");
        }
    }
}
