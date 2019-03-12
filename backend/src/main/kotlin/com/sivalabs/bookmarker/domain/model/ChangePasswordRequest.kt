package com.sivalabs.bookmarker.domain.model

import javax.validation.constraints.NotBlank

data class ChangePasswordRequest(
    @field:NotBlank(message = "Old password cannot be blank")
    var oldPassword: String = "",

    @field:NotBlank(message = "New password cannot be blank")
    var newPassword: String = ""
)
