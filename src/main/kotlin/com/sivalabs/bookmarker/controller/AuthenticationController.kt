package com.sivalabs.bookmarker.controller

import com.sivalabs.bookmarker.entity.User
import com.sivalabs.bookmarker.model.AuthenticationRequest
import com.sivalabs.bookmarker.model.ChangePassword
import com.sivalabs.bookmarker.model.UserTokenState
import com.sivalabs.bookmarker.security.CustomUserDetailsService
import com.sivalabs.bookmarker.security.SecurityUser
import com.sivalabs.bookmarker.security.TokenHelper
import com.sivalabs.bookmarker.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import javax.servlet.http.HttpServletRequest
import java.security.Principal
import java.util.HashMap

@RestController
@RequestMapping(value = ["/api"])
class AuthenticationController {

    @Autowired
    internal lateinit var tokenHelper: TokenHelper

    @Autowired
    private lateinit var authenticationManager: AuthenticationManager

    @Autowired
    private lateinit var userDetailsService: CustomUserDetailsService

    @Autowired
    private lateinit var userService: UserService

    @Value("\${jwt.expires_in}")
    private var expiredIn: Long = 0

    @PostMapping(value = ["/auth/login"])
    @Throws(AuthenticationException::class)
    fun createAuthenticationToken(@RequestBody authenticationRequest: AuthenticationRequest): ResponseEntity<*> {

        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                authenticationRequest.username,
                authenticationRequest.password
            )
        )

        SecurityContextHolder.getContext().authentication = authentication

        val user = authentication.principal as SecurityUser
        val jws = tokenHelper.generateToken(user.username)
        return ResponseEntity.ok(UserTokenState(jws, expiredIn))
    }

    @PostMapping(value = ["/auth/refresh"])
    fun refreshAuthenticationToken(request: HttpServletRequest, principal: Principal?): ResponseEntity<*> {

        val authToken = tokenHelper.getToken(request)

        return if (authToken != null && principal != null) {
            val refreshedToken = tokenHelper.refreshToken(authToken)
            ResponseEntity.ok(UserTokenState(refreshedToken, expiredIn))
        } else {
            val userTokenState = UserTokenState()
            ResponseEntity.accepted().body(userTokenState)
        }
    }

    @RequestMapping("/me")
    @PreAuthorize("isAuthenticated()")
    fun user(user: Principal): User? {
        return this.userService.findByEmail(user.name)
    }

    @PostMapping(value = ["/change-password"])
    @PreAuthorize("isAuthenticated()")
    fun changePassword(@RequestBody changePassword: ChangePassword): ResponseEntity<*> {
        userDetailsService.changePassword(changePassword.oldPassword, changePassword.newPassword)
        val result = HashMap<String, String>()
        result["result"] = "success"
        return ResponseEntity.accepted().body<Map<String, String>>(result)
    }
}
