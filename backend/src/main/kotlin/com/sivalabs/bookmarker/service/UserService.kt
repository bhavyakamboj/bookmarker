package com.sivalabs.bookmarker.service

import com.sivalabs.bookmarker.config.Loggable
import com.sivalabs.bookmarker.entity.User
import com.sivalabs.bookmarker.model.UserDTO
import com.sivalabs.bookmarker.repo.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.Optional

@Service
@Transactional
@Loggable
class UserService(private val userRepository: UserRepository) {

    fun getAllUsers(): List<UserDTO> = userRepository.findAll().map { UserDTO.fromEntity(it) }

    fun getUserById(id: Long): Optional<UserDTO> {
        return userRepository.findById(id).map { UserDTO.fromEntity(it) }
    }

    fun createUser(user: User): UserDTO {
        return UserDTO.fromEntity(userRepository.save(user))
    }

    fun updateUser(user: User): UserDTO {
        return UserDTO.fromEntity(userRepository.save(user))
    }

    fun deleteUser(userId: Long) {
        userRepository.findById(userId).map { userRepository.delete(it) }
    }

    fun findByEmail(email: String): User? {
        return userRepository.findByEmail(email)
    }
}
