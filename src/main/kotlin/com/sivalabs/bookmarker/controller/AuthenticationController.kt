package com.sivalabs.bookmarker.controller

import com.sivalabs.bookmarker.entity.User
import com.sivalabs.bookmarker.model.AuthenticationRequest
import com.sivalabs.bookmarker.model.ChangePassword
import com.sivalabs.bookmarker.model.UserTokenState
import com.sivalabs.bookmarker.security.CustomUserDetailsService
import com.sivalabs.bookmarker.security.SecurityUser
import com.sivalabs.bookmarker.security.SecurityUtils
import com.sivalabs.bookmarker.security.TokenHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
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

    @Value("\${jwt.expires_in}")
    private var expiresIn: Long = 0

    @PostMapping(value = ["/auth/login"])
    fun createAuthenticationToken(@RequestBody credentials: AuthenticationRequest): UserTokenState {
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(credentials.username, credentials.password)
        )

        SecurityContextHolder.getContext().authentication = authentication

        val user = authentication.principal as SecurityUser
        val jws = tokenHelper.generateToken(user.username)
        return UserTokenState(jws, expiresIn)
    }

    @PostMapping(value = ["/auth/refresh"])
    @PreAuthorize("isAuthenticated()")
    fun refreshAuthenticationToken(request: HttpServletRequest): ResponseEntity<UserTokenState> {
        val authToken = tokenHelper.getToken(request)
        return if (authToken != null) {
            val email = tokenHelper.getUsernameFromToken(authToken)
            val userDetails = userDetailsService.loadUserByUsername(email)
            val validToken = tokenHelper.validateToken(authToken, userDetails)
            if (validToken) {
                val refreshedToken = tokenHelper.refreshToken(authToken)
                ResponseEntity.ok(UserTokenState(refreshedToken, expiresIn))
            } else {
                ResponseEntity.status(HttpStatus.UNAUTHORIZED).build<UserTokenState>()
            }
        } else {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).build<UserTokenState>()
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
