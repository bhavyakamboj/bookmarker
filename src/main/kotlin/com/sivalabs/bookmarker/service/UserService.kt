package com.sivalabs.bookmarker.service

import com.sivalabs.bookmarker.config.Loggable
import com.sivalabs.bookmarker.entity.User
import com.sivalabs.bookmarker.model.UserProfile
import com.sivalabs.bookmarker.repo.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.Optional

@Service
@Transactional
@Loggable
class UserService(private val userRepository: UserRepository) {

    fun getAllUsers(): List<UserProfile> = userRepository.findAll().map { UserProfile.fromEntity(it) }

    fun getUserById(id: Long): Optional<User> {
        return userRepository.findById(id)
    }

    fun createUser(user: User): UserProfile {
        return UserProfile.fromEntity(userRepository.save(user))
    }

    fun updateUser(user: User): UserProfile {
        return UserProfile.fromEntity(userRepository.save(user))
    }

    fun deleteUser(userId: Long) {
        userRepository.findById(userId).map { userRepository.delete(it) }
    }

    fun getUserProfile(id: Long): Optional<UserProfile> {
        return this.getUserById(id).map { UserProfile.fromEntity(it) }
    }

    fun findByEmail(email: String): User? {
        return userRepository.findByEmail(email)
    }
}
