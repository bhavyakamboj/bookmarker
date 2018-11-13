package com.sivalabs.bookmarker.model

data class ChangePassword(
    var oldPassword: String = "",
    var newPassword: String = ""
)
