package com.sivalabs.bookmarker.users

import com.sivalabs.bookmarker.config.BookmarkerProperties
import com.sivalabs.bookmarker.users.model.AuthenticationRequest
import com.sivalabs.bookmarker.users.model.ChangePassword
import com.sivalabs.bookmarker.users.model.AuthenticationResponse
import com.sivalabs.bookmarker.config.security.CustomUserDetailsService
import com.sivalabs.bookmarker.users.model.SecurityUser
import com.sivalabs.bookmarker.utils.SecurityUtils
import com.sivalabs.bookmarker.config.security.TokenHelper
import com.sivalabs.bookmarker.users.entity.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
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
    private lateinit var userService: UserService

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    private lateinit var userDetailsService: CustomUserDetailsService

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

    @PostMapping(value = ["/change-password"])
    @PreAuthorize("hasRole('ROLE_USER')")
    fun changePassword(@RequestBody changePassword: ChangePassword) {
        val currentUser = SecurityContextHolder.getContext().authentication
        val email = currentUser.name

        authenticationManager.authenticate(UsernamePasswordAuthenticationToken(email, changePassword.oldPassword))

        userService.findByEmail(email)?.also { user ->
            user.password = passwordEncoder.encode(changePassword.newPassword)
            userService.updateUser(user)
        }
    }
}
