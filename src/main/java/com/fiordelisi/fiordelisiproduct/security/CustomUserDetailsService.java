package com.fiordelisi.fiordelisiproduct.security;

import com.fiordelisi.fiordelisiproduct.config.FiordelisiProductUserPrincipal;
import com.fiordelisi.fiordelisiproduct.entity.User;
import com.fiordelisi.fiordelisiproduct.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findFirstByUsername(username).orElseThrow(() -> new RuntimeException("User not found with username: " + username));

        log.info("loadUserByUsername: {}", username);
        return new FiordelisiProductUserPrincipal(user);
    }
}