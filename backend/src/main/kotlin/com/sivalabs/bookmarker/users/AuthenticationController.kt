package com.sivalabs.bookmarker.users

import com.sivalabs.bookmarker.config.BookmarkerProperties
import com.sivalabs.bookmarker.config.security.CustomUserDetailsService
import com.sivalabs.bookmarker.config.security.TokenHelper
import com.sivalabs.bookmarker.users.entity.User
import com.sivalabs.bookmarker.users.model.AuthenticationRequest
import com.sivalabs.bookmarker.users.model.AuthenticationResponse
import com.sivalabs.bookmarker.users.model.SecurityUser
import com.sivalabs.bookmarker.utils.SecurityUtils
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping(value = ["/api"])
class AuthenticationController(
    private val authenticationManager: AuthenticationManager,
    private val userDetailsService: CustomUserDetailsService,
    private val tokenHelper: TokenHelper,
    private val bookmarkerProperties: BookmarkerProperties
) {

    @PostMapping(value = ["/auth/login"])
    fun createAuthenticationToken(@RequestBody credentials: AuthenticationRequest): AuthenticationResponse {
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(credentials.username, credentials.password)
        )

        SecurityContextHolder.getContext().authentication = authentication

        val user = authentication.principal as SecurityUser
        val jws = tokenHelper.generateToken(user.username)
        return AuthenticationResponse(jws, bookmarkerProperties.jwt.expiresIn)
    }

    @PostMapping(value = ["/auth/refresh"])
    @PreAuthorize("hasRole('ROLE_USER')")
    fun refreshAuthenticationToken(request: HttpServletRequest): ResponseEntity<AuthenticationResponse> {
        val authToken = tokenHelper.getToken(request)
        return if (authToken != null) {
            val email = tokenHelper.getUsernameFromToken(authToken)
            val userDetails = userDetailsService.loadUserByUsername(email)
            val validToken = tokenHelper.validateToken(authToken, userDetails)
            if (validToken) {
                val refreshedToken = tokenHelper.refreshToken(authToken)
                ResponseEntity.ok(AuthenticationResponse(refreshedToken, bookmarkerProperties.jwt.expiresIn))
            } else {
                ResponseEntity.status(HttpStatus.UNAUTHORIZED).build<AuthenticationResponse>()
            }
        } else {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).build<AuthenticationResponse>()
        }
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('ROLE_USER')")
    fun me(): ResponseEntity<User> {
        return SecurityUtils.loginUser()?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
    }
}
