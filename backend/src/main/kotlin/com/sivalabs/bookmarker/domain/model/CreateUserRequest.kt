package com.sivalabs.bookmarker.domain.model

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

data class CreateUserRequest(
    @field:NotBlank(message = "Name cannot be blank")
    var name: String,

    @field:NotBlank(message = "Email cannot be blank")
    @field:Email(message = "Invalid email address")
    var email: String,

    @field:NotBlank(message = "Password cannot be blank")
    var password: String
)
