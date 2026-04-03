package com.mohith.financedatatracker.config;

import com.mohith.financedatatracker.model.User;
import com.mohith.financedatatracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DatabaseSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            log.info("No users found. Seeding default users for evaluation...");

            User admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .role(User.Role.ADMIN)
                    .isActive(true)
                    .build();

            User analyst = User.builder()
                    .username("analyst")
                    .password(passwordEncoder.encode("analyst123"))
                    .role(User.Role.ANALYST)
                    .isActive(true)
                    .build();

            User viewer = User.builder()
                    .username("viewer")
                    .password(passwordEncoder.encode("viewer123"))
                    .role(User.Role.VIEWER)
                    .isActive(true)
                    .build();

            userRepository.save(admin);
            userRepository.save(analyst);
            userRepository.save(viewer);

            log.info("Default users seeded successfully! (admin, analyst, viewer | password: [role]123)");
        }
    }
}