package com.sivalabs.bookmarker.model

import com.fasterxml.jackson.annotation.JsonProperty

data class UserTokenState(
    @JsonProperty("access_token")
    var accessToken: String = "",

    @JsonProperty("expires_in")
    var expiresIn: Long = 0
)
