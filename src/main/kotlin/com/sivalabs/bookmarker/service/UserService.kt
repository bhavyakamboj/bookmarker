package com.sivalabs.bookmarker.service

import com.sivalabs.bookmarker.config.Loggable
import com.sivalabs.bookmarker.entity.User
import com.sivalabs.bookmarker.model.UserDTO
import com.sivalabs.bookmarker.model.UserProfile
import com.sivalabs.bookmarker.repo.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.Optional

@Service
@Transactional
@Loggable
class UserService(private val userRepository: UserRepository) {

    fun getAllUsers(): List<UserDTO> = userRepository.findAll().map { UserDTO.fromEntity(it) }

    fun getUserById(id: Long): Optional<User> {
        return userRepository.findById(id)
    }

    fun createUser(user: UserDTO): UserDTO {
        return UserDTO.fromEntity(userRepository.save(user.toEntity()))
    }

    fun updateUser(user: UserDTO): UserDTO {
        return UserDTO.fromEntity(userRepository.save(user.toEntity()))
    }

    fun deleteUser(userId: Long) {
        userRepository.findById(userId).map { userRepository.delete(it) }
    }

    fun getUserProfile(id: Long): Optional<UserProfile> {
        val userOptional = this.getUserById(id)
        if (!userOptional.isPresent) {
            return Optional.empty()
        }
        val user = userOptional.get()
        return Optional.of(UserProfile(user.id, user.name, user.email))
    }
}
