package com.sivalabs.bookmarker.web.controller;

import com.sivalabs.bookmarker.domain.exception.ResourceNotFoundException;
import com.sivalabs.bookmarker.domain.model.ChangePasswordRequest;
import com.sivalabs.bookmarker.domain.model.CreateUserRequest;
import com.sivalabs.bookmarker.domain.model.UserDTO;
import com.sivalabs.bookmarker.domain.service.UserService;
import com.sivalabs.bookmarker.web.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
        log.info("process=get_user, user_id="+id);
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("")
    @ResponseStatus(CREATED)
    public UserDTO createUser(@RequestBody @Valid CreateUserRequest createUserRequest) {
        log.info("process=create_user, user_email="+createUserRequest.getEmail());
        UserDTO userDTO = new UserDTO(
                null,
                createUserRequest.getName(),
                createUserRequest.getEmail(),
                createUserRequest.getPassword(),
                null
        );
        return userService.createUser(userDTO);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/{id}")
    public UserDTO updateUser(@PathVariable Long id, @RequestBody @Valid UserDTO user) {
        log.info("process=update_user, user_id="+id);
        if (!id.equals(SecurityUtils.loginUserId())) {
            throw new ResourceNotFoundException("User not found with id="+id);
        } else {
            user.setId(id);
            return userService.updateUser(user);
        }
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        log.info("process=delete_user, user_id="+id);
        userService.getUserById(id).map ( u -> {
            if (!id.equals(SecurityUtils.loginUserId()) && !SecurityUtils.isCurrentUserAdmin()) {
                throw new ResourceNotFoundException("User not found with id="+id);
            } else {
                userService.deleteUser(id);
            }
            return u;
        });
    }

    @PostMapping("/change-password")
    @PreAuthorize("hasRole('ROLE_USER')")
    public void changePassword(@RequestBody @Valid ChangePasswordRequest changePasswordRequest) {
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        String email = currentUser.getName();
        log.info("process=change_password, email="+email);
        userService.changePassword(email, changePasswordRequest);
    }
}
