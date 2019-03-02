package com.sivalabs.bookmarker.config.security

import com.sivalabs.bookmarker.domain.repository.UserRepository
import com.sivalabs.bookmarker.domain.model.SecurityUser
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(private val userRepository: UserRepository) : UserDetailsService {

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByEmail(username)
        return if (user.isPresent) {
            SecurityUser(user.get())
        } else {
            throw UsernameNotFoundException("No user found with username $username.")
        }
    }
}
