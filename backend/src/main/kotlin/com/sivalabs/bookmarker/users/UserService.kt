package com.sivalabs.bookmarker.users

import com.sivalabs.bookmarker.config.Loggable
import com.sivalabs.bookmarker.exception.IncorrectPasswordException
import com.sivalabs.bookmarker.exception.UserNotFoundException
import com.sivalabs.bookmarker.users.entity.User
import com.sivalabs.bookmarker.users.model.ChangePassword
import com.sivalabs.bookmarker.users.model.UserDTO
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.Optional

@Service
@Transactional
@Loggable
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {

    @Transactional(readOnly = true)
    fun getUserById(id: Long): Optional<UserDTO> {
        return userRepository.findById(id).map { UserDTO.fromEntity(it) }
    }

    @Transactional(readOnly = true)
    fun getUserByEmail(email: String): Optional<User> {
        return userRepository.findByEmail(email)
    }

    fun createUser(user: User): UserDTO {
        user.password = passwordEncoder.encode(user.password)
        return UserDTO.fromEntity(userRepository.save(user))
    }

    fun updateUser(user: User): UserDTO {
        return userRepository.findById(user.id).map {
            user.password = it.password
            UserDTO.fromEntity(userRepository.save(user))
        }.orElseThrow { UserNotFoundException("User not found") }
    }

    fun deleteUser(userId: Long) {
        userRepository.findById(userId).map { userRepository.delete(it) }
    }

    fun changePassword(email: String, changePassword: ChangePassword) {
        this.getUserByEmail(email).map { user ->
            if (passwordEncoder.matches(changePassword.oldPassword, user.password)) {
                user.password = passwordEncoder.encode(changePassword.newPassword)
                updateUser(user)
            } else {
                throw IncorrectPasswordException("Current password doesn't match")
            }
        }.orElseThrow { UserNotFoundException("User not found") }
    }
}