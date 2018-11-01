package com.sivalabs.myapp.model

import com.sivalabs.myapp.entity.User

import java.time.LocalDateTime

class UserDTO {

    var id: Long? = null
    var name: String? = null
    var email: String? = null
    var githubUsername: String? = null
    var createdAt: LocalDateTime? = null
    var updatedAt: LocalDateTime? = null

    fun toEntity(): User {
        val user = User()

        user.id = (id)
        user.name = (name)
        user.email = (email)
        user.githubUsername = (githubUsername)
        user.createdAt = (createdAt)
        user.updatedAt = (updatedAt)
        return user
    }

    companion object {

        fun fromEntity(user: User): UserDTO {
            val dto = UserDTO()
            dto.id = user.id
            dto.name = user.name
            dto.email = user.email
            dto.githubUsername = user.githubUsername
            dto.createdAt = user.createdAt
            dto.updatedAt = user.updatedAt

            return dto
        }
    }
}
