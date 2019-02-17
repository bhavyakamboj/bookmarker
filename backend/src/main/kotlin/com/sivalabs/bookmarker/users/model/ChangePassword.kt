package com.sivalabs.bookmarker.users.model

import javax.validation.constraints.NotBlank

data class ChangePassword(
    @field:NotBlank(message = "Old password cannot be blank")
    var oldPassword: String = "",
    @field:NotBlank(message = "New password cannot be blank")
    var newPassword: String = ""
)
