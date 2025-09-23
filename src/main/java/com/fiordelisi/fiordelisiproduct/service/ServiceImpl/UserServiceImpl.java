package com.fiordelisi.fiordelisiproduct.service.ServiceImpl;

import com.fiordelisi.fiordelisiproduct.dto.UserDto;
import com.fiordelisi.fiordelisiproduct.entity.User;
import com.fiordelisi.fiordelisiproduct.repository.UserRepository;
import com.fiordelisi.fiordelisiproduct.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Page<User> search(String query, Pageable pageable) {
        if (query == null || query.isBlank()) {
            return userRepository.findAll(pageable);
        }
        return userRepository.findByUsernameContainingIgnoreCase(query.trim(), pageable);
    }

    @Override
    public Optional<User> findById(String id) {
        return userRepository.findById(id);
    }

    @Override
    public User create(User user) {
        user.setId(null);
        if (user.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userRepository.save(user);
    }

    @Override
    public User update(String id, User user) {
        User existing = userRepository.findById(id).orElseThrow();
        existing.setUsername(user.getUsername());
        existing.setEmail(user.getEmail());
        if (user.getPassword() != null && !user.getPassword().isBlank()) {
            existing.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userRepository.save(existing);
    }

    @Override
    public void deleteById(String id) {
        userRepository.deleteById(id);
    }

    @Override
    public void changePassword(String userId, String currentPassword, String newPassword) {
        User user = userRepository.findById(userId).orElseThrow();
        if (user.getPassword() != null && !passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public User saveFromDto(UserDto dto) {
        User user = User.builder()
                .id(dto.getId())
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .build();
        if (dto.getId() == null || dto.getId().isBlank()) {
            return create(user);
        }
        return update(dto.getId(), user);
    }

    @Override
    public UserDto getUserDtoForForm(String id) {
        if (id == null) return new UserDto();
        User u = findById(id).orElse(new User());
        return UserDto.builder()
                .id(u.getId())
                .username(u.getUsername())
                .email(u.getEmail())
                .build();
    }
}


