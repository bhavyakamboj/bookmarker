package com.sivalabs.bookmarker.domain.model

import javax.validation.constraints.NotBlank

data class AuthenticationRequest(
    @field:NotBlank(message = "UserName cannot be blank")
    var username: String = "",

    @field:NotBlank(message = "Password cannot be blank")
    var password: String = ""
)
