package com.sivalabs.bookmarker.domain.service;

import com.sivalabs.bookmarker.domain.entity.Role;
import com.sivalabs.bookmarker.domain.entity.User;
import com.sivalabs.bookmarker.domain.exception.BookmarkerException;
import com.sivalabs.bookmarker.domain.exception.ResourceNotFoundException;
import com.sivalabs.bookmarker.domain.model.ChangePasswordRequest;
import com.sivalabs.bookmarker.domain.model.UserDTO;
import com.sivalabs.bookmarker.domain.repository.RoleRepository;
import com.sivalabs.bookmarker.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Optional<UserDTO> getUserById(Long id) {
        return userRepository.findById(id).map(UserDTO::fromEntity);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public UserDTO createUser(UserDTO user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new BookmarkerException("Email ${user.email} is already in use");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User userEntity = user.toEntity();
        Optional<Role> role_user = roleRepository.findByName("ROLE_USER");
        userEntity.setRoles(Collections.singleton(role_user.orElse(null)));
        return UserDTO.fromEntity(userRepository.save(userEntity));
    }

    public UserDTO updateUser(UserDTO user) {
        Optional<User> byId = userRepository.findById(user.getId());
        if(!byId.isPresent()) {
            throw new ResourceNotFoundException("User with id ${user.id} not found");
        }
        User userEntity = user.toEntity();
        userEntity.setPassword(byId.get().getPassword());
        userEntity.setRoles(byId.get().getRoles());
        return UserDTO.fromEntity(userRepository.save(userEntity));

    }

    public void deleteUser(Long userId) {
        Optional<User> byId = userRepository.findById(userId);
        byId.ifPresent(userRepository::delete);
    }

    public void changePassword(String email, ChangePasswordRequest changePasswordRequest) {
        Optional<User> userByEmail = this.getUserByEmail(email);
        if(!userByEmail.isPresent()) {
            throw new ResourceNotFoundException("User with email $email not found");
        }
        User user = userByEmail.get();
        if (passwordEncoder.matches(changePasswordRequest.getOldPassword(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
            userRepository.save(user);
        } else {
            throw new BookmarkerException("Current password doesn't match");
        }
    }
}
