package com.sivalabs.bookmarker.domain.model

import com.sivalabs.bookmarker.domain.entity.User
import org.springframework.security.core.authority.SimpleGrantedAuthority

class SecurityUser(val user: User) : org.springframework.security.core.userdetails.User(
    user.email,
    user.password,
    user.roles.map { SimpleGrantedAuthority(it.name) }
)
