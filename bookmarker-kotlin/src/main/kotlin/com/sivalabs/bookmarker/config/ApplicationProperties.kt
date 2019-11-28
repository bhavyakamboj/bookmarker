package com.sivalabs.bookmarker.config

import com.sivalabs.bookmarker.domain.utils.Constants.DEFAULT_JWT_TOKEN_EXPIRES
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties(prefix = "application")
@ConstructorBinding
data class ApplicationProperties(
    var jwt: JwtConfig = JwtConfig()
)

data class JwtConfig(
    var issuer: String = "bookmarker",
    var header: String = "Authorization",
    var expiresIn: Long = DEFAULT_JWT_TOKEN_EXPIRES,
    var secret: String = ""
)
