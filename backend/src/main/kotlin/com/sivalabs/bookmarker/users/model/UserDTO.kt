package com.sivalabs.bookmarker.users.model

import com.sivalabs.bookmarker.users.entity.User

data class UserDTO(
    var id: Long,
    var name: String,
    var email: String,
    var roles: List<String>
) {
    companion object {

        fun fromEntity(user: User): UserDTO {
            return UserDTO(user.id, user.name, user.email, user.roles.map { it.name })
        }
    }
}
