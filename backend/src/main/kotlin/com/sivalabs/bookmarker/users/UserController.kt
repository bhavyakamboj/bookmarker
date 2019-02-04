package com.sivalabs.bookmarker.users

import com.sivalabs.bookmarker.config.Loggable
import com.sivalabs.bookmarker.users.entity.User
import com.sivalabs.bookmarker.users.model.UserDTO
import com.sivalabs.bookmarker.utils.logger
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.ResponseEntity
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
class UserController(private val userService: UserService) {

    private val log = logger()

    @GetMapping("/{id}")
    fun getUser(@PathVariable id: Long): ResponseEntity<UserDTO> {
        log.info("process=get-user, user_id={}", id)
        return userService.getUserById(id)
                .map { ResponseEntity.ok(it) }
                .orElse(ResponseEntity.notFound().build())
    }

    @PostMapping("")
    @ResponseStatus(CREATED)
    fun createUser(@RequestBody user: User): UserDTO {
        log.info("process=create-user, user_email={}", user.email)
        return userService.createUser(user)
    }

    @PutMapping("/{id}")
    fun updateUser(@PathVariable id: Long, @RequestBody user: User): UserDTO {
        log.info("process=update-user, user_id={}", id)
        user.id = id
        return userService.updateUser(user)
    }

    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: Long) {
        log.info("process=delete-user, user_id={}", id)
        userService.deleteUser(id)
    }
}
