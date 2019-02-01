package com.sivalabs.bookmarker.security

import com.sivalabs.bookmarker.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService : UserDetailsService {

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    private lateinit var authenticationManager: AuthenticationManager

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userService.findByEmail(username)
        return if (user == null) {
            throw UsernameNotFoundException("No user found with username $username.")
        } else {
            SecurityUser(user)
        }
    }

    fun changePassword(oldPassword: String, newPassword: String) {

        val currentUser = SecurityContextHolder.getContext().authentication
        val email = currentUser.name

        authenticationManager.authenticate(UsernamePasswordAuthenticationToken(email, oldPassword))

        userService.findByEmail(email)?.also { user ->
            user.password = passwordEncoder.encode(newPassword)
            userService.updateUser(user)
        }
    }
}
