package com.sivalabs.bookmarker.users.model

data class ChangePassword(
    var oldPassword: String = "",
    var newPassword: String = ""
)
