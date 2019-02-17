package com.sivalabs.bookmarker.config

import com.sivalabs.bookmarker.utils.Constants.DEFAULT_JWT_TOKEN_EXPIRES
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "bookmarker")
class BookmarkerProperties {
    var jwt: JwtConfig = JwtConfig()

    class JwtConfig {
        var issuer: String = "bookmarker"
        var header: String = "Authorization"
        var expiresIn: Long = DEFAULT_JWT_TOKEN_EXPIRES
        var secret: String = ""
    }
}
