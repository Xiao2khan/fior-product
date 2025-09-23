package com.fiordelisi.fiordelisiproduct.service;

import com.fiordelisi.fiordelisiproduct.dto.UserDto;
import com.fiordelisi.fiordelisiproduct.entity.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> findAll();

    Page<User> search(String query, Pageable pageable);

    Optional<User> findById(String id);

    User create(User user);

    User update(String id, User user);

    void deleteById(String id);

    void changePassword(String userId, String currentPassword, String newPassword);

    User saveFromDto(UserDto dto);
    UserDto getUserDtoForForm(String id);
}

