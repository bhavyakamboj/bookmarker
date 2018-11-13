package com.sivalabs.bookmarker.security

import com.sivalabs.bookmarker.entity.User
import com.sivalabs.bookmarker.repo.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class SecurityUtils(private val userRepository: UserRepository) {

    fun loginUser(): User? {
        val authentication = SecurityContextHolder.getContext().authentication
        if (authentication != null) {
            val user = authentication.principal as SecurityUser
            return userRepository.findByEmail(user.username)
        }
        return null
    }
}
