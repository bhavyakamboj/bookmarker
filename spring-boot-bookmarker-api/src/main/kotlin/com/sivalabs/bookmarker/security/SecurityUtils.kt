package com.sivalabs.bookmarker.security

import com.sivalabs.bookmarker.entity.User
import org.springframework.security.core.context.SecurityContextHolder

object SecurityUtils {

    fun loginUser(): User? {
        val authentication = SecurityContextHolder.getContext().authentication
        if (authentication != null && authentication.principal is SecurityUser) {
            val securityUser = authentication.principal as SecurityUser
            return securityUser.user
        }
        return null
    }
}
