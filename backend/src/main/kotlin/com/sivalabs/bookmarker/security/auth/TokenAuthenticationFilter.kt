package com.sivalabs.bookmarker.security.auth

import com.sivalabs.bookmarker.security.TokenHelper
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.web.filter.OncePerRequestFilter

import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import java.io.IOException

class TokenAuthenticationFilter(
    private val tokenHelper: TokenHelper,
    private val userDetailsService: UserDetailsService
)
    : OncePerRequestFilter() {

    @Throws(IOException::class, ServletException::class)
    public override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain
    ) {
        val authToken = tokenHelper.getToken(request)

        if (authToken != null) {
            val username = tokenHelper.getUsernameFromToken(authToken)
            val userDetails = userDetailsService.loadUserByUsername(username)
            if (tokenHelper.validateToken(authToken, userDetails)) {
                val authentication = TokenBasedAuthentication(authToken, userDetails)
                SecurityContextHolder.getContext().authentication = authentication
            }
        }
        chain.doFilter(request, response)
    }
}
