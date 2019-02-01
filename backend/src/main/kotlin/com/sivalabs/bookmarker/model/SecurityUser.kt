package com.sivalabs.bookmarker.model

import org.springframework.security.core.authority.SimpleGrantedAuthority

class SecurityUser(val user: com.sivalabs.bookmarker.entity.User)
    : org.springframework.security.core.userdetails.User(
            user.email,
            user.password,
            user.roles.map { SimpleGrantedAuthority(it.name) }
        )
