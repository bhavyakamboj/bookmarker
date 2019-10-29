package com.sivalabs.bookmarker.web.controller

import com.sivalabs.bookmarker.config.ApplicationProperties
import com.sivalabs.bookmarker.config.security.CustomUserDetailsService
import com.sivalabs.bookmarker.config.security.TokenHelper
import com.sivalabs.bookmarker.domain.model.AuthenticationRequest
import com.sivalabs.bookmarker.domain.model.AuthenticationResponse
import com.sivalabs.bookmarker.config.security.SecurityUser
import com.sivalabs.bookmarker.domain.model.UserDTO
import com.sivalabs.bookmarker.web.utils.SecurityUtils
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
@RequestMapping(value = ["/api/auth"])
class AuthenticationController(
    private val authenticationManager: AuthenticationManager,
    private val userDetailsService: CustomUserDetailsService,
    private val tokenHelper: TokenHelper,
    private val applicationProperties: ApplicationProperties
) {

    @PostMapping(value = ["/login"])
    fun createAuthenticationToken(@RequestBody credentials: AuthenticationRequest): AuthenticationResponse {
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(credentials.username, credentials.password)
        )

        SecurityContextHolder.getContext().authentication = authentication

        val user = authentication.principal as SecurityUser
        val jws = tokenHelper.generateToken(user.username)
        return AuthenticationResponse(jws, applicationProperties.jwt.expiresIn)
    }

    @PostMapping(value = ["/refresh"])
    @PreAuthorize("hasRole('ROLE_USER')")
    fun refreshAuthenticationToken(request: HttpServletRequest): ResponseEntity<AuthenticationResponse> {
        val authToken = tokenHelper.getToken(request)
        return if (authToken != null) {
            val email = tokenHelper.getUsernameFromToken(authToken)
            val userDetails = userDetailsService.loadUserByUsername(email)
            val validToken = tokenHelper.validateToken(authToken, userDetails)
            if (validToken) {
                val refreshedToken = tokenHelper.refreshToken(authToken)
                ResponseEntity.ok(
                    AuthenticationResponse(
                        refreshedToken,
                        applicationProperties.jwt.expiresIn
                    )
                )
            } else {
                ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
            }
        } else {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        }
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('ROLE_USER')")
    fun me(): ResponseEntity<UserDTO> {
        return SecurityUtils.loginUser()?.let {
            ResponseEntity.ok(UserDTO.fromEntity(it))
        } ?: ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
    }
}
