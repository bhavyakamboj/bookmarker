package com.sivalabs.bookmarker.web.controller

import com.sivalabs.bookmarker.domain.annotation.Loggable
import com.sivalabs.bookmarker.web.exception.BadRequestException
import com.sivalabs.bookmarker.domain.exception.UserNotFoundException
import com.sivalabs.bookmarker.domain.model.ChangePassword
import com.sivalabs.bookmarker.domain.model.CreateUserRequest
import com.sivalabs.bookmarker.domain.model.UserDTO
import com.sivalabs.bookmarker.domain.service.UserService
import com.sivalabs.bookmarker.web.utils.SecurityUtils
import com.sivalabs.bookmarker.domain.utils.logger
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
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
import javax.validation.Valid

@RestController
@RequestMapping("/api/users")
@Loggable
class UserController(
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
    fun createUser(@RequestBody @Valid createUserRequest: CreateUserRequest): UserDTO {
        log.info("process=create_user, user_email=${createUserRequest.email}")
        val userDTO = UserDTO(
            id = 0L,
            name = createUserRequest.name,
            email = createUserRequest.email,
            password = createUserRequest.password,
            roles = listOf()
        )
        return userService.createUser(userDTO)
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/{id}")
    fun updateUser(@PathVariable id: Long, @RequestBody @Valid user: UserDTO): UserDTO {
        log.info("process=update_user, user_id=$id")
        if (id != SecurityUtils.loginUser()?.id && !SecurityUtils.isCurrentUserAdmin()) {
            throw BadRequestException("You can't mess with other user details")
        } else {
            user.id = id
            return userService.updateUser(user)
        }
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: Long) {
        log.info("process=delete_user, user_id=$id")

        userService.getUserById(id).map { u ->
            if (u == null || (u.id != SecurityUtils.loginUser()?.id &&
                        !SecurityUtils.isCurrentUserAdmin())
            ) {
                throw UserNotFoundException("User not found with id=$id")
            } else {
                userService.deleteUser(id)
            }
        }
    }

    @PostMapping("/change-password")
    @PreAuthorize("hasRole('ROLE_USER')")
    fun changePassword(@RequestBody @Valid changePassword: ChangePassword) {
        val currentUser = SecurityContextHolder.getContext().authentication
        val email = currentUser.name
        log.info("process=change_password, email=$email")
        userService.changePassword(email, changePassword)
    }
}
