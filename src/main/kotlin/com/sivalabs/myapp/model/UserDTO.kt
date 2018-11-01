package com.sivalabs.myapp.model

import com.sivalabs.myapp.entity.User

import java.time.LocalDateTime

data class UserDTO (
    var id: Long,
    var name: String,
    var email: String,
    var githubUsername: String,
    var createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime? = LocalDateTime.now()
) {
    fun toEntity(): User {
        val user = User()

        user.id = id
        user.name = name
        user.email = email
        user.githubUsername = githubUsername
        user.createdAt = createdAt
        user.updatedAt = updatedAt
        return user
    }

    companion object {

        fun fromEntity(user: User): UserDTO {
            return UserDTO(user.id,user.name,user.email,user.githubUsername,user.createdAt,user.updatedAt)
        }
    }
}
