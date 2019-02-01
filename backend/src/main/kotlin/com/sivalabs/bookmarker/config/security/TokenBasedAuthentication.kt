package com.sivalabs.bookmarker.config.security

import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails

class TokenBasedAuthentication(
    private val token: String,
    private val principle: UserDetails
) : AbstractAuthenticationToken(principle.authorities) {

    override fun isAuthenticated() = true

    override fun getCredentials() = token

    override fun getPrincipal() = principle
}
