package com.sivalabs.bookmarker.users

import com.sivalabs.bookmarker.config.Loggable
import com.sivalabs.bookmarker.exception.ResourceNotFoundException
import com.sivalabs.bookmarker.users.entity.User
import com.sivalabs.bookmarker.users.model.ChangePassword
import com.sivalabs.bookmarker.users.model.UserDTO
import com.sivalabs.bookmarker.utils.SecurityUtils
import com.sivalabs.bookmarker.utils.logger
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
@Loggable
class UserController(
    private val authenticationManager: AuthenticationManager,
    private val userService: UserService
) {

    private val log = logger()

    @GetMapping("/{id}")
    fun getUser(@PathVariable id: Long): ResponseEntity<UserDTO> {
        log.info("process=get_user, user_id=$id")
        return userService.getUserById(id)
                .map { ResponseEntity.ok(it) }
                .orElse(ResponseEntity.notFound().build())
    }

    @PostMapping("")
    @ResponseStatus(CREATED)
    fun createUser(@RequestBody user: User): UserDTO {
        log.info("process=create_user, user_email=${user.email}")
        return userService.createUser(user)
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/{id}")
    fun updateUser(@PathVariable id: Long, @RequestBody user: User): UserDTO {
        log.info("process=update_user, user_id=$id")
        user.id = id
        return userService.updateUser(user)
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: Long) {
        log.info("process=delete_user, user_id=$id")

        userService.getUserById(id).map { u ->
            if (u == null || (u.id != SecurityUtils.loginUser()?.id &&
                            !SecurityUtils.isCurrentUserAdmin())) {
                throw ResourceNotFoundException("User not found with id=$id")
            } else {
                userService.deleteUser(id)
            }
        }
    }

    @PostMapping("/change-password")
    @PreAuthorize("hasRole('ROLE_USER')")
    fun changePassword(@RequestBody changePassword: ChangePassword) {
        val currentUser = SecurityContextHolder.getContext().authentication
        val email = currentUser.name
        log.info("process=change_password, email=$email")
        authenticationManager.authenticate(UsernamePasswordAuthenticationToken(email, changePassword.oldPassword))
        userService.changePassword(email, changePassword.newPassword)
    }
}
