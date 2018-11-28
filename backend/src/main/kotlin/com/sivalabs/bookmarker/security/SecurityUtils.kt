package com.sivalabs.bookmarker.security

import com.sivalabs.bookmarker.entity.User
import com.sivalabs.bookmarker.service.UserService
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component

@Component
class SecurityUtils(private val userService: UserService) {

    fun loginUser(): User? {
        val authentication = SecurityContextHolder.getContext().authentication
        if (authentication != null && authentication.principal is UserDetails) {
            val user = authentication.principal as UserDetails
            return userService.findByEmail(user.username)
        }
        return null
    }
}
