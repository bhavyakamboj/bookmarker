package com.sivalabs.bookmarker.users.model

import com.sivalabs.bookmarker.users.entity.User
import org.springframework.security.core.authority.SimpleGrantedAuthority

class SecurityUser(val user: User)
    : org.springframework.security.core.userdetails.User(
            user.email,
            user.password,
            user.roles.map { SimpleGrantedAuthority(it.name) }
        )
