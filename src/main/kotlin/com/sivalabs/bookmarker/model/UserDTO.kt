package com.sivalabs.bookmarker.model

import com.sivalabs.bookmarker.entity.User

import java.time.LocalDateTime

data class UserDTO(
    var id: Long,
    var name: String,
    var email: String,
    var password: String,
    var createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime? = LocalDateTime.now()
) {
    fun toEntity(): User {
        val user = User()

        user.id = id
        user.name = name
        user.email = email
        user.password = password
        user.createdAt = createdAt
        user.updatedAt = updatedAt
        return user
    }

    companion object {

        fun fromEntity(user: User): UserDTO {
            return UserDTO(user.id, user.name, user.email, user.password, user.createdAt, user.updatedAt)
        }
    }
}
