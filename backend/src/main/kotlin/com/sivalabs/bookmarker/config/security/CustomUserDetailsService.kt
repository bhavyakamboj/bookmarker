package com.sivalabs.bookmarker.config.security

import com.sivalabs.bookmarker.users.model.SecurityUser
import com.sivalabs.bookmarker.users.UserService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(val userService: UserService) : UserDetailsService {

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userService.findByEmail(username)
        return if (user == null) {
            throw UsernameNotFoundException("No user found with username $username.")
        } else {
            SecurityUser(user)
        }
    }
}
