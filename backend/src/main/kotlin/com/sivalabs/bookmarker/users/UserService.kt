package com.sivalabs.bookmarker.users

import com.sivalabs.bookmarker.config.Loggable
import com.sivalabs.bookmarker.exception.BookmarkerException
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
    private val roleRepository: RoleRepository,
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

    fun createUser(user: UserDTO): UserDTO {
        if (userRepository.existsByEmail(user.email)) {
            throw BookmarkerException("Email ${user.email} is already in use")
        }
        user.password = passwordEncoder.encode(user.password)
        val userEntity = user.toEntity()
        roleRepository.findByName("ROLE_USER").map { userEntity.roles = mutableListOf(it) }
        return UserDTO.fromEntity(userRepository.save(userEntity))
    }

    fun updateUser(user: UserDTO): UserDTO {
        return userRepository.findById(user.id).map {
            val userEntity = user.toEntity()
            userEntity.password = it.password
            userEntity.roles = it.roles
            UserDTO.fromEntity(userRepository.save(userEntity))
        }.orElseThrow { UserNotFoundException("User not found") }
    }

    fun deleteUser(userId: Long) {
        userRepository.findById(userId).map { userRepository.delete(it) }
    }

    fun changePassword(email: String, changePassword: ChangePassword) {
        this.getUserByEmail(email).map { user ->
            if (passwordEncoder.matches(changePassword.oldPassword, user.password)) {
                user.password = passwordEncoder.encode(changePassword.newPassword)
                userRepository.save(user)
            } else {
                throw BookmarkerException("Current password doesn't match")
            }
        }.orElseThrow { UserNotFoundException("User not found") }
    }
}
