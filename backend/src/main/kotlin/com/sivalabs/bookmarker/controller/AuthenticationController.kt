package com.sivalabs.bookmarker.controller

import com.sivalabs.bookmarker.config.BookmarkerProperties
import com.sivalabs.bookmarker.entity.User
import com.sivalabs.bookmarker.model.AuthenticationRequest
import com.sivalabs.bookmarker.model.ChangePassword
import com.sivalabs.bookmarker.model.AuthenticationResponse
import com.sivalabs.bookmarker.security.CustomUserDetailsService
import com.sivalabs.bookmarker.security.SecurityUser
import com.sivalabs.bookmarker.security.SecurityUtils
import com.sivalabs.bookmarker.security.TokenHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping(value = ["/api"])
class AuthenticationController {

    @Autowired
    private lateinit var tokenHelper: TokenHelper

    @Autowired
    private lateinit var authenticationManager: AuthenticationManager

    @Autowired
    private lateinit var userDetailsService: CustomUserDetailsService

    @Autowired
    private lateinit var securityUtils: SecurityUtils

    @Autowired
    private lateinit var bookmarkerProperties: BookmarkerProperties

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
    @PreAuthorize("isAuthenticated()")
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
    @PreAuthorize("isAuthenticated()")
    fun me(): ResponseEntity<User> {
        return securityUtils.loginUser()?.let { ResponseEntity.ok(it) }
                ?: ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
    }

    @PostMapping(value = ["/change-password"])
    @PreAuthorize("isAuthenticated()")
    fun changePassword(@RequestBody changePassword: ChangePassword) {
        userDetailsService.changePassword(changePassword.oldPassword, changePassword.newPassword)
    }
}
