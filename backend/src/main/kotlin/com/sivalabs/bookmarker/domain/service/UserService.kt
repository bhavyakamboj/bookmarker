package com.sivalabs.bookmarker.domain.service

import com.sivalabs.bookmarker.domain.exception.BookmarkerException
import com.sivalabs.bookmarker.domain.exception.UserNotFoundException
import com.sivalabs.bookmarker.domain.entity.User
import com.sivalabs.bookmarker.domain.repository.RoleRepository
import com.sivalabs.bookmarker.domain.repository.UserRepository
import com.sivalabs.bookmarker.domain.model.ChangePasswordRequest
import com.sivalabs.bookmarker.domain.model.UserDTO
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.Optional

@Service
@Transactional
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
        }.orElseThrow { UserNotFoundException("User with id ${user.id} not found") }
    }

    fun deleteUser(userId: Long) {
        userRepository.findById(userId).map { userRepository.delete(it) }
    }

    fun changePassword(email: String, changePasswordRequest: ChangePasswordRequest) {
        this.getUserByEmail(email).map { user ->
            if (passwordEncoder.matches(changePasswordRequest.oldPassword, user.password)) {
                user.password = passwordEncoder.encode(changePasswordRequest.newPassword)
                userRepository.save(user)
            } else {
                throw BookmarkerException("Current password doesn't match")
            }
        }.orElseThrow { UserNotFoundException("User with email $email not found") }
    }
}
