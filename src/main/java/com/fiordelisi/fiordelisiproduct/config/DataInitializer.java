package com.fiordelisi.fiordelisiproduct.config;

import com.fiordelisi.fiordelisiproduct.entity.User;
import com.fiordelisi.fiordelisiproduct.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        try {
            initializeUsers();
        } catch (Exception e) {
            log.error("Error during initialization: {}", e.getMessage(), e);
        }
    }

    @Transactional
    protected void initializeUsers() {
        try {
            if (userRepository.findFirstByUsername("admin").isEmpty()) {
                User admin = User.builder()
                        .username("admin")
                        .email("admin@gmail.com")
                        .password(passwordEncoder.encode("admin"))
                        .build();
                userRepository.save(admin);
            }
        } catch (Exception e) {
            log.error("Error initializing users: {}", e.getMessage());
            throw e;
        }
    }
}
