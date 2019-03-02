package com.sivalabs.bookmarker.domain.model

import com.fasterxml.jackson.annotation.JsonProperty
import com.sivalabs.bookmarker.domain.entity.User
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

data class UserDTO(
    var id: Long,
    @NotBlank(message = "Name cannot be blank")
    var name: String,
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email address")
    var email: String,
    @NotBlank(message = "Password cannot be blank")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    var password: String?,
    var roles: List<String>
) {
    companion object {

        fun fromEntity(user: User): UserDTO {
            return UserDTO(
                user.id,
                user.name,
                user.email,
                user.password,
                user.roles.map { it.name })
        }
    }

    fun toEntity(): User {
        val user = User()
        user.id = id
        user.name = name
        user.email = email
        user.password = password ?: ""
        return user
    }
}
