package com.sivalabs.bookmarker.utils

import com.sivalabs.bookmarker.users.entity.User
import com.sivalabs.bookmarker.users.model.SecurityUser
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

    fun isCurrentUserAdmin(): Boolean {
        return loginUser()?.roles?.any { it.name == "ROLE_ADMIN" } ?: false
    }
}
