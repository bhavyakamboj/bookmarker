package com.sivalabs.bookmarker.users.model

data class AuthenticationRequest(
    var username: String = "",
    var password: String = ""
)
