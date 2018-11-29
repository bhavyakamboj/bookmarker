package com.sivalabs.bookmarker.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "bookmarker")
class BookmarkerProperties {
    var logstashHost: String = "localhost"
    var jwt: JwtConfig = JwtConfig()

    class JwtConfig {
        var issuer: String = "bookmarker"
        var header: String = "Authorization"
        var expiresIn: Long = 604800
        var secret: String = ""
    }
}
