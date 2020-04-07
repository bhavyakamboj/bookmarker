package com.sivalabs.bookmarker.config.security;

import com.sivalabs.bookmarker.domain.entity.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.stream.Collectors;

public class SecurityUser extends org.springframework.security.core.userdetails.User {
    private User user;

    public SecurityUser(User user) {
        super(user.getEmail(), user.getPassword(),
                user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList()));
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
